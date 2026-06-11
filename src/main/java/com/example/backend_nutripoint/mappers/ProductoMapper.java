package com.example.backend_nutripoint.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.backend_nutripoint.DTO.responses.ImageResponseDTO;
import com.example.backend_nutripoint.DTO.responses.ProductCardDTO;
import com.example.backend_nutripoint.DTO.responses.ProductDetailResponseDTO;
import com.example.backend_nutripoint.DTO.responses.ProductResponseDTO;
import com.example.backend_nutripoint.models.Categoria;
import com.example.backend_nutripoint.models.Producto;

public class ProductoMapper {

    private ProductoMapper() {
    }

    public static ProductResponseDTO productToDTO(Producto prod, List<ImageResponseDTO> images) {
        return ProductResponseDTO.builder()
                .idProducto(prod.getIdProducto())
                .nombre(prod.getNombre())
                .descripcion(prod.getDescripcion())
                .stock(prod.getStock())
                .marca(prod.getMarca().getNombre())
                .preciounit(prod.getPrecioUnit())
                .modEmpleo(prod.getModEmpleo())
                .advert(prod.getAdvert())
                .imagenes(images)
                // .imagenesUrls(imagenesUrls)
                .categorias(prod.getCategorias().stream().map(Categoria::getCategoria).toList())
                .build();
    }

    public static ProductDetailResponseDTO productDetailToDTO(ProductResponseDTO productDto,
            Set<ProductCardDTO> relatedProducts) {
        return ProductDetailResponseDTO.builder()
                .product(productDto)
                .relatedProducts(relatedProducts)
                .build();
    }

    public static Set<ProductCardDTO> productCardToDTO(List<Producto> relatedProducts) {
        return relatedProducts.stream()
                .map(p -> ProductCardDTO.builder()
                        .idProducto(p.getIdProducto())
                        .nombre(p.getNombre())
                        .precio(p.getPrecioUnit())
                        .categorias(p.getCategorias().stream().map(Categoria::getCategoria).toList())
                        .imagenUrl(getFirstImage(p))
                        .build())
                .collect(Collectors.toSet());
    }

    private static String getFirstImage(Producto producto) {
        if (producto.getImagenes() == null ||
                producto.getImagenes().isEmpty()) {
            return null;
        }

        return producto.getImagenes()
                .get(0)
                .getImageUrl();
    }

}
