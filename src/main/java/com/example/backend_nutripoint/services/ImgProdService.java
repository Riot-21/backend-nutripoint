package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/jpg");

    private static final long MAX_SIZE_IN_BYTES = 2 * 1024 * 1024;

    @Value("${app.image.base-url}")
    private String baseUrl;

    @Transactional
    public List<String> uploadImage(List<MultipartFile> files, Integer productoId) throws IOException {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        List<ImgProd> imagenes = new ArrayList<>();

        for(MultipartFile file: files){
            validateImage(file);
        
        // Convertir archivo a bytes
        byte[] imageData = file.getBytes();

        // Guardar en la BD
        ImgProd imgProd = new ImgProd();
        imgProd.setImage(imageData);
        imgProd.setContentType(file.getContentType());
        imgProd.setProducto(producto);
        
        imagenes.add(imgProd);
        }

        imgProdRepository.saveAll(imagenes);
        // Devolver URL simulada
        return secureURLImages(imagenes);
        // return baseUrl + imgProd.getIdImg();
    }

    public byte[] getImageById(Integer idImg) {
        ImgProd imgProd = imgProdRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        return imgProd.getImage();
    }

    public String getType(Integer idImg) {
        ImgProd imgProd = imgProdRepository.findById(idImg)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        return imgProd.getContentType();
    }

    public void deleteImage(Integer idImg) {
        imgProdRepository.deleteById(idImg);
    }

    public List<String> getImagesByProductId(Integer idProducto) {
        List<ImgProd> imagenes = imgProdRepository.findByProductoIdProducto(idProducto);

        if (imagenes.isEmpty()) {
            throw new RuntimeException("No se encontraron imágenes para este producto.");
        }

        // Convertir la lista de imágenes en URLs
        return secureURLImages(imagenes);
        // .toList(); CREO QUE PUEDE PONERSE SIMPLEMENTE ASI
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

    private List<String> secureURLImages(List<ImgProd> imagenes){
        return imagenes.stream()
                .map(img -> baseUrl + img.getIdImg())
                .toList();
                // .collect(Collectors.toList());
    }

}
