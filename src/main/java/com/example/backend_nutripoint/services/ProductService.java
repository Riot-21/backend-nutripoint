package com.example.backend_nutripoint.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.requests.ProductCreateDTO;
import com.example.backend_nutripoint.DTO.requests.ProductFilterDTO;
import com.example.backend_nutripoint.DTO.requests.ProductUpdateDTO;
import com.example.backend_nutripoint.DTO.responses.PriceRangeDTO;
import com.example.backend_nutripoint.DTO.responses.ProductResponseDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.mappers.ProductoMapper;
import com.example.backend_nutripoint.models.Categoria;
import com.example.backend_nutripoint.models.ImgProd;
import com.example.backend_nutripoint.models.Marca;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.CategoryRepository;
import com.example.backend_nutripoint.repositories.MarcaRepository;
import com.example.backend_nutripoint.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductoRepository productoRepository;
    private final CategoryRepository categoryRepository;
    private final MarcaRepository marcaRepository;
    private final ImgProdService imgProdService;

    // Metodo para traer las marcas

    // Metodo principal para traer productos paginados por defecto
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(ProductFilterDTO filterDTO) {
        Specification<Producto> spec = Specification.unrestricted();

        // Filtro por nombre o descripción
        if (filterDTO.getQuery() != null && !filterDTO.getQuery().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("nombre")), "%" + filterDTO.getQuery().toLowerCase() + "%")));
                    // cb.like(cb.lower(root.get("descripcion")), "%" + filterDTO.getQuery().toLowerCase() + "%")));
        }

        // Filtro por marca
        // if (filterDTO.getMarca() != null && !filterDTO.getMarca().isBlank()) {
        // spec = spec.and(
        // (root, query, cb) -> cb.equal(cb.lower(root.get("marca")),
        // filterDTO.getMarca().toLowerCase()));
        // }

        // Filtro por múltiples marcas
        if (filterDTO.getMarcas() != null && !filterDTO.getMarcas().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                if (query != null)
                    query.distinct(true);
                return cb.lower(root.get("marca").get("nombre")).in(
                        filterDTO.getMarcas()
                                .stream()
                                .map(String::toLowerCase)
                                .toList());
            });
        }

        // filtro por categorias
        if (filterDTO.getCategorias() != null && !filterDTO.getCategorias().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                if (query != null)
                    query.distinct(true);
                return cb.lower(root.get("categorias").get("categoria")).in(
                        filterDTO.getCategorias()
                                .stream().map(String::toLowerCase)
                                .toList());
            });
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

        if (filterDTO.getPage() == null) {
            filterDTO.setPage(0);
        }
        if (filterDTO.getSize() == null) {
            filterDTO.setSize(10);
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

        return productosPage.map(product -> ProductoMapper.productToDTO(product, getImageUrlsFromEntity(product)));
    }

    // Metodo para crear un producto
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto) throws IOException {
        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El producto con nombre: " + dto.getNombre() + " ya existe.");
        }
        Producto prod = new Producto();
        prod.setNombre(dto.getNombre());
        prod.setDescripcion(dto.getDescripcion());
        prod.setStock(dto.getStock());
        // prod.setMarca(dto.getMarca());
        prod.setPrecioUnit(dto.getPrecioUnit());
        prod.setModEmpleo(dto.getModEmpleo());
        prod.setAdvert(dto.getAdvert());

        Set<Categoria> categorias = validateCategories(dto.getCategorias());
        prod.setCategorias(categorias);
        Marca marca = validateMarca(dto.getMarca());
        prod.setMarca(marca);

        Producto savedProduct = productoRepository.save(prod);
        List<String> imagenesURL = new ArrayList<>();

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            imagenesURL = imgProdService.uploadImage(dto.getImagenes(), savedProduct.getIdProducto());
        }

        return ProductoMapper.productToDTO(savedProduct, imagenesURL);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Integer id, ProductUpdateDTO dto) {
        @SuppressWarnings("null")
        Producto prod = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: " + id));
        if (dto.getNombre() != null)
            prod.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null)
            prod.setDescripcion(dto.getDescripcion());
        if (dto.getStock() != null)
            prod.setStock(dto.getStock());
        if (dto.getMarca() != null) {
            Marca marca = validateMarca(dto.getMarca());
            prod.setMarca(marca);
        }
        if (dto.getPrecioUnit() != null)
            prod.setPrecioUnit(dto.getPrecioUnit());
        if (dto.getModEmpleo() != null)
            prod.setModEmpleo(dto.getModEmpleo());
        if (dto.getAdvert() != null)
            prod.setAdvert(dto.getAdvert());

        if (dto.getCategorias() != null && !dto.getCategorias().isEmpty()) {
            Set<Categoria> categorias = validateCategories(dto.getCategorias());
            prod.setCategorias(categorias);
        }

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            imgProdService.uploadImage(dto.getImagenes(), prod.getIdProducto());
        }

        Producto updatedProduct = productoRepository.save(prod);
        List<String> imagenes = getImageUrlsFromEntity(updatedProduct);
        return ProductoMapper.productToDTO(updatedProduct, imagenes);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado. ID: " + id));
        return ProductoMapper.productToDTO(producto, getImageUrlsFromEntity(producto));
    }

    @Transactional
    public void deleteProducto(Integer id) {
        Producto prod = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ese producto"));

        List<ImgProd> imagenes = prod.getImagenes();
        if (imagenes != null && !imagenes.isEmpty()) {
            for (ImgProd img : imagenes) {
                imgProdService.deleteImage(img.getIdImg());
            }
        }
        productoRepository.delete(prod);
    }

    @Transactional
    public PriceRangeDTO getPriceRange(){
        Double min = productoRepository.findMinPrice();
        Double max = productoRepository.findMaxPrice();

        if(min==null || max==null){
            return new PriceRangeDTO(0.0, 0.0);
        }

        return new PriceRangeDTO(min, max);
    }

    // Metodo que trae las imagenes segun el producto
    private List<String> getImageUrlsFromEntity(Producto product) {
        if (product.getImagenes() == null || product.getImagenes().isEmpty()) {
            return List.of();
        }
        return product.getImagenes().stream()
                .map(ImgProd::getImageUrl)
                .toList();
    }

    // Metodo que filtra las categorias antes de crear o actualizar un producto
    // Las categorias se asignan, osea tienen que existir
    // Se usa distinct() para evitar mandar duplicados, igualmente el tipo
    // categorias es set(tambien evita duplicados)
    private Set<Categoria> validateCategories(List<String> categorias) {
        return categorias.stream()
                .distinct()
                .map(cat -> categoryRepository.findByCategoria(cat)
                        .orElseThrow(() -> new IllegalArgumentException("La categoria: " + cat + " no existe")))
                .collect(Collectors.toSet());
    }

    private Marca validateMarca(String marca) {
        Marca m = marcaRepository.findByNombre(marca)
                .orElseThrow(() -> new IllegalArgumentException("La marca no existe"));

        return m;
    }
}
