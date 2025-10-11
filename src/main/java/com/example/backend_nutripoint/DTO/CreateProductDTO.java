package com.example.backend_nutripoint.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 1, message = "El stock debe ser mayor a 0")
    private Integer stock;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioUnit;

    @NotBlank(message = "El modo de empleo es obligatorio")
    private String modEmpleo;

    @NotBlank(message = "Las advertencias son obligatorias")
    private String advert;

    @Size(max = 3, message = "Solo se permiten hasta 3 imágenes")
    private List<MultipartFile> imagenes;
}
