package com.example.backend_nutripoint.DTO.requests;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateDTO {

    @Size(min = 1, message = "no debe estar vacio")
    private String nombre;

    @Size(min = 1, message = "no debe estar vacio")
    private String descripcion;

    @Min(value = 1, message = "El stock debe ser mayor a 0")
    private Integer stock;

    @Size(min = 1, message = "no debe estar vacio")
    private String marca;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 dígitos enteros y 2 decimales")
    // private Double precioUnit;
    private BigDecimal precioUnit;

    @Size(min = 1, message = "no debe estar vacio")
    private String modEmpleo;

    @Size(min = 1, message = "no debe estar vacio")
    private String advert;

    // @NotEmpty(message = "Debe haber al menos 1 categoria")
    @Size(min = 1, message = "Debe haber minimo 1 categoria")
    private List<@Size(min = 1, message = "debe ser una categoria valida, no vacia") String> categorias;

    @Size(max = 3, message = "Solo se permiten hasta 3 imágenes")
    private List<MultipartFile> imagenes;
}
