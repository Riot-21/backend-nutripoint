package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.ImgProdRepository;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import jakarta.transaction.Transactional;
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

    @Value("${app.image.base-url}")
    private String baseUrl;

    @Transactional
    public List<String> uploadImage(List<MultipartFile> files, Integer productoId) throws IOException {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        List<ImgProd> imagenesActuales = imgProdRepository.findByProductoIdProducto(productoId);
        if(imagenesActuales.size() + files.size() > 3){
            throw new RuntimeException("Solo puede haber un maximo de 3 imagenes por producto");
        }
        
        List<ImgProd> imagenes = new ArrayList<>();

        for(MultipartFile file: files){
            validateImage(file);

        // definir el tipo de map como Map<String, Object>
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "folder", "productos/" + productoId,
            "resource_type", "image"
        ));
        String imageUrl = uploadResult.get("secure_url").toString();
        
        // Convertir archivo a bytes
        // byte[] imageData = file.getBytes();

        // Guardar en la BD
        ImgProd imgProd = new ImgProd();
        // imgProd.setImage(imageData);
        imgProd.setImageUrl(imageUrl);
        imgProd.setContentType(file.getContentType());
        imgProd.setProducto(producto);
        
        imagenes.add(imgProd);
        }

        imgProdRepository.saveAll(imagenes);
        // Devolver URL simulada
        return imagenes.stream().map(ImgProd::getImageUrl).toList();
        // return secureURLImages(imagenes);
    }

    public List<String> getImagesByProductId(Integer idProducto) {
        List<ImgProd> imagenes = imgProdRepository.findByProductoIdProducto(idProducto);
        if (imagenes.isEmpty()) {
            throw new RuntimeException("No se encontraron imágenes para este producto.");
        }
        return imagenes.stream().map(ImgProd::getImageUrl).toList();
    }

    // Metodo para imagenes en blob
    // public byte[] getImageById(Integer idImg) {
    //     ImgProd imgProd = imgProdRepository.findById(idImg)
    //             .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    //     return imgProd.getImage();
    // }

    public String getType(Integer idImg) {
        ImgProd imgProd = imgProdRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        return imgProd.getContentType();
    }

        public void deleteImage(Integer idImg) {
        ImgProd imgProd = imgProdRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // Extraer el public_id desde la URL para borrar en Cloudinary
        String publicId = extractPublicId(imgProd.getImageUrl());
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Error eliminando imagen de Cloudinary: " + e.getMessage());
        }

        imgProdRepository.deleteById(idImg);
    }

    // public void deleteImage(Integer idImg) {
    //     imgProdRepository.deleteById(idImg);
    // }

    // public List<String> getImagesByProductId(Integer idProducto) {
    //     List<ImgProd> imagenes = imgProdRepository.findByProductoIdProducto(idProducto);

    //     if (imagenes.isEmpty()) {
    //         throw new RuntimeException("No se encontraron imágenes para este producto.");
    //     }

    //     // Convertir la lista de imágenes en URLs
    //     return secureURLImages(imagenes);
    //     // .toList(); CREO QUE PUEDE PONERSE SIMPLEMENTE ASI
    // }

    private void validateImage(MultipartFile file) {
        String mimeType = file.getContentType();
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Tipo de archivo no soportado. Solo JPEG, JPG o PNG.");
        }
        if (file.getSize() > MAX_SIZE_IN_BYTES) {
            throw new IllegalArgumentException("El archivo excede el máximo de 2MB.");
        }
    }

    private List<String> secureURLImages(List<ImgProd> imagenes){
        return imagenes.stream()
                .map(img -> baseUrl + img.getIdImg())
                .toList();
                // .collect(Collectors.toList());
    }
        // Extrae el public_id de la URL de Cloudinary
    private String extractPublicId(String url) {
        // Ejemplo: https://res.cloudinary.com/demo/image/upload/v1234/productos/123/abcde.jpg
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1]; // abcde.jpg
        return "productos/" + parts[parts.length - 2] + "/" + filename.replace(".jpg", "");
    }

}
