package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductoRepository productoRepository;
    private final ImgProdService imgProdService;

    @Value("${app.image.base-url}")
    private String imageUrl;

    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO dto) throws IOException {
        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El producto con nombre: " + dto.getNombre() + " ya existe.");
        }
        Producto prod = new Producto();
        prod.setNombre(dto.getNombre());
        prod.setDescripcion(dto.getDescripcion());
        prod.setStock(dto.getStock());
        prod.setMarca(dto.getMarca());
        prod.setPrecioUnit(dto.getPrecioUnit());
        prod.setModEmpleo(dto.getModEmpleo());
        prod.setAdvert(dto.getAdvert());

        Producto savedProduct = productoRepository.save(prod);
        List<String> imagenesURL = new ArrayList<>();

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            imagenesURL = imgProdService.uploadImage(dto.getImagenes(), savedProduct.getIdProducto());
        }

        return mapToDTO(savedProduct, imagenesURL);
    }

    public List<ProductResponseDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(product -> mapToDTO(product, imagenesWithSecureUrl(product.getImagenes()))).toList();
    }

    public ProductResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: " + id));
        return mapToDTO(producto, imagenesWithSecureUrl(producto.getImagenes()));
    }

    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    private List<String> imagenesWithSecureUrl(List<ImgProd> imagenes) {
        List<String> imagenesUrls = imagenes != null
                ? imagenes.stream()
                        .map(img -> imageUrl + img.getIdImg())
                        .toList()
                : List.of();

        return imagenesUrls;
    }

    private ProductResponseDTO mapToDTO(Producto prod, List<String> imagenesUrls) {
        return ProductResponseDTO.builder()
                .idProducto(prod.getIdProducto())
                .nombre(prod.getNombre())
                .descripcion(prod.getDescripcion())
                .stock(prod.getStock())
                .marca(prod.getMarca())
                .preciounit(prod.getPrecioUnit())
                .modEmpleo(prod.getModEmpleo())
                .advert(prod.getAdvert())
                .imagenesUrls(imagenesUrls)
                .build();
    }
}
