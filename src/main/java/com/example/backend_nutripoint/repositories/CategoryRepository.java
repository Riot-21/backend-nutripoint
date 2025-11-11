package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.Categoria;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByCategoria(String categoria);
    boolean existsByCategoria(String categoria);
}

