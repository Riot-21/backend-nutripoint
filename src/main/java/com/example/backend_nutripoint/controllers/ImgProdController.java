package com.example.backend_nutripoint.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_nutripoint.services.ImgProdService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/imagenes")
@RequiredArgsConstructor
public class ImgProdController {

    private final ImgProdService imgProdService;

    @GetMapping("/all/{idProducto}")
    public ResponseEntity<Object> getImagesByProduct(@PathVariable Integer idProducto) {
        try {
            List<String> imageUrls = imgProdService.getImagesByProductId(idProducto);
            return ResponseEntity.ok(imageUrls);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/upload/{productoId}")
    public ResponseEntity<Object> uploadImage(
            @PathVariable Integer productoId,
            @RequestParam("file") List<MultipartFile> files) throws IOException {
            
        if (files.size() > 3) {
            return ResponseEntity.badRequest().body("No se pueden subir más de 3 imágenes.");
        }
        
            List<String> imageUrls = imgProdService.uploadImage(files, productoId);

            return ResponseEntity.ok(imageUrls); // Devolver la URL simulada
        
    }

    // @GetMapping("/{idImg}")
    // public ResponseEntity<byte[]> getImage(@PathVariable Integer idImg) {
    //     try {
    //         byte[] imageData = imgProdService.getImageById(idImg);
    //         String type = imgProdService.getType(idImg);
    //         return ResponseEntity.ok()
    //                 .contentType(MediaType.parseMediaType(type)) // Ajusta según el tipo de imagen
    //                 .body(imageData);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //     }
    // }

    @DeleteMapping("/{idImg}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer idImg) {
        try {
            imgProdService.deleteImage(idImg);
            return ResponseEntity.noContent().build(); // Respuesta exitosa
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Imagen no encontrada
        }
    }
}
