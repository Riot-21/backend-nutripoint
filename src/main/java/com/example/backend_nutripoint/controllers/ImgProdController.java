package com.example.backend_nutripoint.controllers;

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
        List<String> imageUrls = imgProdService.getImagesByProductId(idProducto);
        return ResponseEntity.ok(imageUrls);
    }

    @PostMapping("/upload/{productoId}")
    public ResponseEntity<Object> uploadImage(
            @PathVariable Integer productoId,
            @RequestParam("file") List<MultipartFile> files) {

        if (files.size() > 3) {
            throw new IllegalArgumentException("No se pueden subir más de 3 imágenes.");
        }

        List<String> imageUrls = imgProdService.uploadImage(files, productoId);
        return ResponseEntity.ok(imageUrls); 

    }

    @DeleteMapping("/{idImg}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer idImg) {
        try {
            imgProdService.deleteImage(idImg);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
}
