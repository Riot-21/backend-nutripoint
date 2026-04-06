package com.example.backend_nutripoint.DTO.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarcaCreateDTO {

    @NotBlank
    private String marca;
}
