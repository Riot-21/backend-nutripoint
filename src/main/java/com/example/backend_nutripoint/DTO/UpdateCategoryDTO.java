package com.example.backend_nutripoint.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryDTO {  
    
    @Size(min = 1, message = "no debe estar vacio")
    private String categoria;

    @Size(min = 1, message = "no debe estar vacio")
    private String objetivo;
}
