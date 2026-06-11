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
public class ProductCardDTO {

    private Integer idProducto;

    private String nombre;

    private BigDecimal precio;
    private List<String> categorias;

    private String imagenUrl;
}
