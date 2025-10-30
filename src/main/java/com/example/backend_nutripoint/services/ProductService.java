package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.ProductFilterDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductoRepository productoRepository;
    private final ImgProdService imgProdService;

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(ProductFilterDTO filterDTO) {
        Specification<Producto> spec = Specification.unrestricted();

        // Filtro por nombre o descripción
        if (filterDTO.getQuery() != null && !filterDTO.getQuery().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("nombre")), "%" + filterDTO.getQuery().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("descripcion")), "%" + filterDTO.getQuery().toLowerCase() + "%")));
        }

        // Filtro por marca
        if (filterDTO.getMarca() != null && !filterDTO.getMarca().isBlank()) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(cb.lower(root.get("marca")), filterDTO.getMarca().toLowerCase()));
        }

        // Filtro por rango de precios
        if (filterDTO.getPrecioMin() != null) {
            spec = spec.and(
                    (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("precioUnit"), filterDTO.getPrecioMin()));
        }
        if (filterDTO.getPrecioMax() != null) {
            spec = spec
                    .and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("precioUnit"), filterDTO.getPrecioMax()));
        }

        // Paginación y orden dinámico
        Sort sort = "desc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getSortBy()).descending()
                : Sort.by(filterDTO.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        // Ejecutar consulta
        Page<Producto> productosPage = productoRepository.findAll(spec, pageable);
        if (productosPage.isEmpty()) {
            throw new NotFoundException("No se encontraron resultados para su búsqueda");
        }

        return productosPage.map(product -> mapToDTO(product, getImageUrlsFromEntity(product)));
    }

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

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: " + id));
        return mapToDTO(producto, getImageUrlsFromEntity(producto));
    }

    @Transactional
    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
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

    private List<String> getImageUrlsFromEntity(Producto product) {
        if (product.getImagenes() == null || product.getImagenes().isEmpty()) {
            return List.of();
        }
        return product.getImagenes().stream()
                .map(ImgProd::getImageUrl)
                .toList();
    }
}
