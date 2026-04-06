package com.example.backend_nutripoint.mappers;

import com.example.backend_nutripoint.DTO.responses.CategoryResponseDTO;
import com.example.backend_nutripoint.models.Categoria;

public class CategoryMapper {

    private CategoryMapper(){}

    public static CategoryResponseDTO categoryToDTO(Categoria cat) {
        return CategoryResponseDTO.builder()
                .idCategory(cat.getIdCategoria())
                .categoria(cat.getCategoria())
                .objetivo(cat.getObjetivo())
                .build();
    }
    
}
