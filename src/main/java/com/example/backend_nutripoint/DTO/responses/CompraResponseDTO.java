package com.example.backend_nutripoint.DTO.responses;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.backend_nutripoint.models.EstadoCompra;
import com.example.backend_nutripoint.models.TipoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompraResponseDTO {
    private Integer idCompra;
    private String codigoCompra;
    private Integer idUsuario;
    private LocalDateTime fecha;
    private BigDecimal total;
    private TipoPago tipoPago;
    private String direccion;
    private String distrito;
    private EstadoCompra estadoCompra;
    private List<DetalleCompraResponseDTO> detalle;

}
