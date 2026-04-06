package com.example.backend_nutripoint.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryResponseDTO {
    private Integer idCategory;
    private String categoria;
    private String objetivo;
}
