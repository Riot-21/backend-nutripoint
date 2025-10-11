package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    boolean existsByNombre(String nombre);
}
