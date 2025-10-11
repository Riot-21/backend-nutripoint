package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_nutripoint.DTO.CreateProductDTO;
import com.example.backend_nutripoint.DTO.ProductResponseDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.ImgProdRepository;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductoRepository productoRepository;
    private final ImgProdRepository imgProdRepository;

    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO dto){
        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El producto con nombre: "+dto.getNombre()+" ya existe.");
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

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            List<ImgProd> imagenes = dto.getImagenes().stream()
                .map(file -> guardarImagen(file, savedProduct))
                .toList();

                imgProdRepository.saveAll(imagenes);
        }

        return mapToDTO(savedProduct);
    }

    public List<ProductResponseDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(this::mapToDTO).toList();
    }

    public ProductResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: "+id));
        return mapToDTO(producto);
    }

    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    private ImgProd guardarImagen(MultipartFile file, Producto producto) {
        try {
            ImgProd imgProd = new ImgProd();
            imgProd.setImage(file.getBytes());
            imgProd.setContentType(file.getContentType());
            imgProd.setProducto(producto);
            return imgProd;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    private ProductResponseDTO mapToDTO(Producto prod) {
        List<String> imagenesUrls = prod.getImagenes() != null
                ? prod.getImagenes().stream()
                    .map(img -> "http://localhost:8080/imagenes/" + img.getIdImg())
                    .collect(Collectors.toList())
                : List.of();

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
