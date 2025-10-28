package com.example.backend_nutripoint.DTO;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductFilterDTO {

    
    private String query;

    private String marca;

    @NumberFormat(style = Style.NUMBER, pattern = "0.00")
    @Min(value = 0, message = "El valor minimo debe ser 0")
    private Double precioMin;

    @NumberFormat(style = Style.NUMBER, pattern = "0.00")
    @Min(value = 0, message = "El valor minimo debe ser 0")
    private Double precioMax;

    @Min(value = 0)
    private Integer page = 0;
    
    @Positive(message = "El tamaño de pagina debe ser mayor a 0")
    private Integer size = 10;

    @Pattern(regexp = "nombre|precioUnit|idProducto", message = "sort value just can be 'nombre', 'precioUnit', 'idProducto'")
    private String sortBy = "idProducto";

    @Pattern(regexp = "asc|desc", message = "direction must be 'asc' or 'desc'")
    private String direction = "asc";
}
