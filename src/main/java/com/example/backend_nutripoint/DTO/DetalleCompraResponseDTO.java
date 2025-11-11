package com.example.backend_nutripoint.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DetalleCompraResponseDTO {
    private Integer idDetalle;
    private Integer idProducto;
    private String nombreProd;
    private Integer cantidad;
    private BigDecimal precioUnit;
    private BigDecimal subtotal;
}
