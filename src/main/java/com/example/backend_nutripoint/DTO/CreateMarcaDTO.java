package com.example.backend_nutripoint.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMarcaDTO {

    @NotBlank
    private String marca;
}
