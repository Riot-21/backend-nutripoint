package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.backend_nutripoint.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>, JpaSpecificationExecutor<Producto> {
    boolean existsByNombre(String nombre);

    @Query("SELECT MIN(p.precioUnit) FROM Producto p")
    Double findMinPrice();

    @Query("SELECT MAX(p.precioUnit) FROM Producto p")
    Double findMaxPrice();
}
