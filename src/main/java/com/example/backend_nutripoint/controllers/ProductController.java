package com.example.backend_nutripoint.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import com.example.backend_nutripoint.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProducto(
            @Valid @ModelAttribute CreateProductDTO dto
            // , BindingResult result
            ) throws IOException {

                // ! NO SE NECESITA VALIDAR CON result.hasErrors(), PORQUE PARA ESO YA ESTA DEFINIDO UNA EXCEPCION PARA @VALID
        // if (result.hasErrors()) {
        //     return validation(result);
        // }
        if (dto.getImagenes() != null && dto.getImagenes().size() > 3) {
            throw new IllegalArgumentException("No se pueden subir más de 3 imágenes.");
        }
        ProductResponseDTO productoDTO = productService.createProduct(dto);
        return ResponseEntity.ok(productoDTO);

    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProductos() {
        return ResponseEntity.ok(productService.getAllProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductoById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
