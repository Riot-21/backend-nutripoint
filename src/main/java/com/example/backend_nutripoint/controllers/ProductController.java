package com.example.backend_nutripoint.controllers;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.cloudinary.http5.api.Response;
import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.PriceRangeDTO;
import com.example.backend_nutripoint.DTO.ProductFilterDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import com.example.backend_nutripoint.DTO.UpdateProductDTO;
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
    public ResponseEntity<ProductResponseDTO> createProducto(
            @Valid @ModelAttribute CreateProductDTO dto) throws IOException {

        if (dto.getImagenes() != null && dto.getImagenes().size() > 3) {
            throw new IllegalArgumentException("No se pueden subir más de 3 imágenes.");
        }
        ProductResponseDTO productoDTO = productService.createProduct(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoDTO);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @Valid @ModelAttribute UpdateProductDTO dto) throws IOException{
        if (dto.getImagenes() != null && dto.getImagenes().size() > 3) {
            throw new IllegalArgumentException("No se pueden subir más de 3 imágenes.");
        }
        ProductResponseDTO prod = productService.updateProduct(id, dto);
        return ResponseEntity.ok(prod);
    }

    @GetMapping
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProductResponseDTO>> listarProductos(
            @Valid @ModelAttribute ProductFilterDTO filterDTO) {
        Page<ProductResponseDTO> result = productService.searchProducts(filterDTO);
        return ResponseEntity.ok(result);
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

    @GetMapping("/price-range")
    public ResponseEntity<PriceRangeDTO> getPriceRange() {
        return ResponseEntity.ok(productService.getPriceRange());
    }
    

}
