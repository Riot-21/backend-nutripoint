package com.example.backend_nutripoint.mappers;

import java.util.List;

import com.example.backend_nutripoint.DTO.responses.ProductResponseDTO;
import com.example.backend_nutripoint.models.Categoria;
import com.example.backend_nutripoint.models.Producto;

public class ProductoMapper {
    
    private ProductoMapper() {}

    public static ProductResponseDTO productToDTO(Producto prod, List<String> imagenesUrls) {
        return ProductResponseDTO.builder()
                .idProducto(prod.getIdProducto())
                .nombre(prod.getNombre())
                .descripcion(prod.getDescripcion())
                .stock(prod.getStock())
                .marca(prod.getMarca().getNombre())
                .preciounit(prod.getPrecioUnit())
                .modEmpleo(prod.getModEmpleo())
                .advert(prod.getAdvert())
                .imagenesUrls(imagenesUrls)
                .categorias(prod.getCategorias().stream().map(Categoria::getCategoria).toList())
                .build();
    }
    
}
