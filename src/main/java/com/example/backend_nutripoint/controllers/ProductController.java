package com.example.backend_nutripoint.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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

import io.jsonwebtoken.io.IOException;
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
            ,BindingResult result
            ) throws IOException {

        if (result.hasErrors()) {
            return validation(result);
        }
        // try{
            ProductResponseDTO productoDTO = productService.createProduct(dto);
            return ResponseEntity.ok(productoDTO);
        // }catch(IllegalArgumentException e){
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Manejo de tipo de archivo no soportado

        // }
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
