package com.example.backend_nutripoint.DTO.responses;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private String marca;
    private BigDecimal preciounit;
    private String modEmpleo;
    private String advert;
    private List<String> categorias;
    private List<String> imagenesUrls;
}
