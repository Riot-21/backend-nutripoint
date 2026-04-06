package com.example.backend_nutripoint.DTO.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryUpdateDTO {  
    
    @Size(min = 1, message = "no debe estar vacio")
    private String categoria;

    @Size(min = 1, message = "no debe estar vacio")
    private String objetivo;
}
