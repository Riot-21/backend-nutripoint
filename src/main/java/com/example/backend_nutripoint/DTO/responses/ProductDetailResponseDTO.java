package com.example.backend_nutripoint.DTO.responses;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class ProductDetailResponseDTO {
    private ProductResponseDTO product;
    private Set<ProductCardDTO> relatedProducts;

}
