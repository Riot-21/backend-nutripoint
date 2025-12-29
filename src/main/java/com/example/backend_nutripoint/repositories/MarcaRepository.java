package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.Marca;
import java.util.Optional;


public interface MarcaRepository extends JpaRepository<Marca, Long>{
    Optional<Marca> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
