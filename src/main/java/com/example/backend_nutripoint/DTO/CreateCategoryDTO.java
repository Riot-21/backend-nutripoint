package com.example.backend_nutripoint.DTO;

import jakarta.validation.constraints.NotBlank;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @Builder
// @AllArgsConstructor
// @NoArgsConstructor
public class CreateCategoryDTO {

    @NotBlank
    private String categoria;

    @NotBlank
    private String objetivo;
}
