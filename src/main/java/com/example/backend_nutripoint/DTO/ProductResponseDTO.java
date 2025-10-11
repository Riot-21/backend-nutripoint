package com.example.backend_nutripoint.DTO;

import java.util.List;

import lombok.*;

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
    private Double preciounit;
    private String modEmpleo;
    private String advert;
    private List<String> imagenesUrls;
}
