package com.example.backend_nutripoint.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.CreateMarcaDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.models.Marca;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.repositories.MarcaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;

    @Transactional(readOnly = true)
    public List<String> getAllBrands() {
        return marcaRepository.findAll().stream()
                .map(Marca::getNombre).toList();
    }

    @Transactional
    public Marca createBrand(CreateMarcaDTO marca) {
        if (marcaRepository.existsByNombre(marca.getMarca())) {
            throw new IllegalArgumentException("La marca con nombre: " + marca.getMarca() + " ya existe.");
        }

        Marca m = new Marca();
        m.setNombre(marca.getMarca());

        return marcaRepository.save(m);
    }

    @Transactional
    public void deleteBrand(Long id) {
        Marca mar = marcaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Marca no existente"));

        if (!mar.getProducto().isEmpty()) {
            String products = mar.getProducto().stream()
                    .map(Producto::getNombre)
                    // .map(p->p.getNombre())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(
                    "No se puede eliminar una marca asociada a productos. Productos: " + products);
        }

        marcaRepository.delete(mar);
    }

}
