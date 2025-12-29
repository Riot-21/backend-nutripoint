package com.example.backend_nutripoint.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.CategoryResponseDTO;
import com.example.backend_nutripoint.DTO.CreateCategoryDTO;
import com.example.backend_nutripoint.DTO.UpdateCategoryDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.Categoria;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> mapToDTO(cat)).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Integer id) {
        Categoria cat = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada. id: " + id));
        return mapToDTO(cat);
    }

    @Transactional
    public CategoryResponseDTO createCategory(CreateCategoryDTO dto) {
        if (categoryRepository.existsByCategoria(dto.getCategoria())) {
            throw new IllegalArgumentException("La categoria con nombre: " + dto.getCategoria() + " ya existe.");
        }

        Categoria categoria = new Categoria();
        categoria.setCategoria(dto.getCategoria());
        categoria.setObjetivo(dto.getObjetivo());

        return mapToDTO(categoryRepository.save(categoria));
    }

    @Transactional
    public CategoryResponseDTO updateCategory(UpdateCategoryDTO cat, Integer id) {
        Categoria categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada. id: " + id));

        if (cat.getCategoria() != null) {
            categoria.setCategoria(cat.getCategoria());
        }
        if (cat.getObjetivo() != null) {
            categoria.setObjetivo(cat.getObjetivo());
        }

        Categoria updatedCat = categoryRepository.save(categoria);
        return mapToDTO(updatedCat);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Categoria cat = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no existente"));

        if (!cat.getProductos().isEmpty()) {
            String products = cat.getProductos().stream()
                    .map(Producto::getNombre)
                    // .map(p->p.getNombre())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "No se puede eliminar una categoria asociada a productos. Productos: " + products);
        }

        // Para romper relacion con la tabla intermedia y sus productos
        // cat.getProductos().forEach(prod-> prod.getCategorias().remove(cat));
        categoryRepository.delete(cat);
    }

    private CategoryResponseDTO mapToDTO(Categoria cat) {
        return CategoryResponseDTO.builder()
                .idCategory(cat.getIdCategoria())
                .categoria(cat.getCategoria())
                .objetivo(cat.getObjetivo())
                .build();
    }

}
