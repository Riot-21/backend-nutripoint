package com.example.backend_nutripoint.mappers;

import com.example.backend_nutripoint.DTO.responses.CompraResponseDTO;
import com.example.backend_nutripoint.DTO.responses.DetalleCompraResponseDTO;
import com.example.backend_nutripoint.models.Compra;
import com.example.backend_nutripoint.models.DetalleCompra;

public class CompraMapper {
    private CompraMapper(){}

    public static CompraResponseDTO compraToDTO(Compra compra) {
        return CompraResponseDTO.builder()
                        .idCompra(compra.getIdCompra())
                        .idUsuario(compra.getUsuario().getIdUsuario())
                        .fecha(compra.getFecha())
                        .total(compra.getTotal())
                        .tipoPago(compra.getTipoPago())
                        .direccion(compra.getDireccion())
                        .distrito(compra.getDistrito())
                        .estadoCompra(compra.getEstado())
                        .detalle(compra.getDetalles().stream()
                                        .map(d -> detalleToDTO(d))
                                        .toList())
                        .build();

    }
    
    private static DetalleCompraResponseDTO detalleToDTO(DetalleCompra d){
        return DetalleCompraResponseDTO.builder()
                .idDetalle(d.getIdDetalle())
                .idProducto(d.getProducto().getIdProducto())
                .nombreProd(d.getProducto().getNombre())
                .cantidad(d.getCantidad())
                .precioUnit(d.getPrecioUni())
                .subtotal(d.getSubtotal())
                .build();
    }
}
