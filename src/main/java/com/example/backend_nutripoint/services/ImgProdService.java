package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backend_nutripoint.exceptions.ImageUploadException;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.ImgProdRepository;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImgProdService {

    private final ImgProdRepository imgProdRepository;
    private final ProductoRepository productoRepository;
    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/jpg");

    private static final long MAX_SIZE_IN_BYTES = 2 * 1024 * 1024;

    @Transactional
    public List<String> uploadImage(List<MultipartFile> files, Integer productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        List<ImgProd> imagenesActuales = imgProdRepository.findByProductoIdProducto(productoId);
        if (imagenesActuales.size() + files.size() > 3) {
            throw new IllegalArgumentException("El producto ya tiene " + imagenesActuales.size() +
                    " imágenes. Solo se permiten 3 en total.");
        }

        List<ImgProd> imagenes = new ArrayList<>();
        // Public ID's para rollback en caso de fallo
        List<String> uploadedPublicIds = new ArrayList<>();

        for (MultipartFile file : files) {
            validateImage(file);

            try {

                // definir el tipo de map como Map<String, Object>
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "productos/" + productoId,
                        "resource_type", "image"));
                uploadedPublicIds.add((String) uploadResult.get("public_id"));
                String imageUrl = uploadResult.get("secure_url").toString();

                ImgProd imgProd = new ImgProd();
                imgProd.setImageUrl(imageUrl);
                imgProd.setContentType(file.getContentType());
                imgProd.setProducto(producto);

                imagenes.add(imgProd);
            } catch (IOException e) {
                for (String publicId : uploadedPublicIds) {
                    try {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

                    } catch (Exception ex) {
                        throw new RuntimeException("Error eliminando imagen de Cloudinary: " + ex.getMessage());
                    }
                }
                throw new ImageUploadException("Error al subir la imagen: " + file.getOriginalFilename(), e);
            }
        }

        imgProdRepository.saveAll(imagenes);
        return imagenes.stream().map(ImgProd::getImageUrl).toList();
    }

    public List<String> getImagesByProductId(Integer idProducto) {
        List<ImgProd> imagenes = imgProdRepository.findByProductoIdProducto(idProducto);
        if (imagenes.isEmpty()) {
            throw new NotFoundException("No se encontraron imágenes para este producto.");
        }
        return imagenes.stream().map(ImgProd::getImageUrl).toList();
    }

    @Transactional
    public void deleteImage(Integer idImg) {
        ImgProd imgProd = imgProdRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // Extraer el public_id desde la URL para borrar en Cloudinary
        String publicId = extractPublicId(imgProd.getImageUrl());
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando imagen de Cloudinary: " + e.getMessage());
        }

        imgProdRepository.deleteById(idImg);
    }


    private void validateImage(MultipartFile file) {
        String mimeType = file.getContentType();
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Tipo de archivo no soportado. Solo JPEG, JPG o PNG.");
        }
        if (file.getSize() > MAX_SIZE_IN_BYTES) {
            throw new IllegalArgumentException("El archivo excede el máximo de 2MB.");
        }
    }

    // Extrae el public_id de la URL de Cloudinary
    private String extractPublicId(String url) {
        // Ejemplo:
        // https://res.cloudinary.com/demo/image/upload/v1234/productos/123/abcde.jpg
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1]; // abcde.jpg
        return "productos/" + parts[parts.length - 2] + "/" + filename.replace(".jpg", "");
    }

}
