package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<ProductResponseDTO> searchProducts(
            String query,
            String marca,
            Double precioMin,
            Double precioMax,
            int page,
            int size,
            String sortBy,
            String direction) {
        Specification<Producto> spec = Specification.unrestricted();

        // 🔸 Filtro por nombre o descripción
        if (query != null && !query.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.or(
                    cb.like(cb.lower(root.get("nombre")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("descripcion")), "%" + query.toLowerCase() + "%")));
        }

        // 🔸 Filtro por marca
        if (marca != null && !marca.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(cb.lower(root.get("marca")), marca.toLowerCase()));
        }

        // 🔸 Filtro por rango de precios
        if (precioMin != null) {
            spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("precioUnit"), precioMin));
        }

        if (precioMax != null) {
            spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("precioUnit"), precioMax));
        }

        if (!"asc".equalsIgnoreCase(direction) && !"desc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException("La dirección de orden debe ser 'asc' o 'desc'.");
        }

        // 🔸 Paginación y orden dinámico
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // 🔸 Ejecutar consulta
        Page<Producto> productosPage = productoRepository.findAll(spec, pageable);

        // 🔸 Mapear entidades a DTOs
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

    public List<ProductResponseDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(product -> mapToDTO(product, imagenesWithSecureUrl(product.getImagenes())))
                .toList();
    }

    public ProductResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: " + id));
        return mapToDTO(producto, getImageUrlsFromEntity(producto));
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

    private List<String> getImageUrlsFromEntity(Producto product) {
        if (product.getImagenes() == null || product.getImagenes().isEmpty()) {
            return List.of();
        }
        return product.getImagenes().stream()
                .map(ImgProd::getImageUrl)
                .toList();
    }
}
