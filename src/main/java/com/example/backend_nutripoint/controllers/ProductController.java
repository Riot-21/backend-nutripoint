package com.example.backend_nutripoint.controllers;

import java.io.IOException;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.ProductFilterDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import org.springframework.security.core.Authentication;
import com.example.backend_nutripoint.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/otro")
    // @PreAuthorize("hasRole('ADMIN')")
    public String hola() {
        return "hola";
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProducto(
            @Valid @ModelAttribute CreateProductDTO dto
    // , BindingResult result
    ) throws IOException {

        // ! NO SE NECESITA VALIDAR CON result.hasErrors(), PORQUE PARA ESO YA ESTA
        // DEFINIDO UNA EXCEPCION PARA @VALID
        // if (result.hasErrors()) {
        // return validation(result);
        // }
        if (dto.getImagenes() != null && dto.getImagenes().size() > 3) {
            throw new IllegalArgumentException("No se pueden subir más de 3 imágenes.");
        }
        ProductResponseDTO productoDTO = productService.createProduct(dto);
        return ResponseEntity.ok(productoDTO);

    }

    // @GetMapping
    // public ResponseEntity<List<ProductResponseDTO>> getAllProductos() {
    // return ResponseEntity.ok(productService.getAllProductos());
    // }

    @GetMapping
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProductResponseDTO>> listarProductos(
            @Valid @ModelAttribute ProductFilterDTO filterDTO) {
        Page<ProductResponseDTO> result = productService.searchProducts(filterDTO);
        return ResponseEntity.ok(result);
    }

    // @GetMapping
    // public ResponseEntity<Page<ProductResponseDTO>> listarProductos(
    // @RequestParam(required = false) String query,
    // @RequestParam(required = false) String marca,
    // @RequestParam(required = false) Double precioMin,
    // @RequestParam(required = false) Double precioMax,
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size,
    // @RequestParam(defaultValue = "nombre") String sortBy,
    // @RequestParam(defaultValue = "asc") String direction) {
    // Page<ProductResponseDTO> result = productService.searchProducts(
    // query, marca, precioMin, precioMax, page, size, sortBy, direction);
    // return ResponseEntity.ok(result);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductoById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    // private ResponseEntity<?> validation(BindingResult result) {
    // Map<String, String> errors = new HashMap<>();
    // result.getFieldErrors().forEach(err -> {
    // errors.put(err.getField(), "El campo " + err.getField() + " " +
    // err.getDefaultMessage());
    // });
    // return ResponseEntity.badRequest().body(errors);
    // }
}
