package com.example.backend_nutripoint.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend_nutripoint.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>, JpaSpecificationExecutor<Producto> {
    boolean existsByNombre(String nombre);

    @Query("SELECT MIN(p.precioUnit) FROM Producto p")
    Double findMinPrice();

    @Query("SELECT MAX(p.precioUnit) FROM Producto p")
    Double findMaxPrice();

    @Query("""
                SELECT p
                FROM Producto p
                JOIN p.categorias c
                WHERE c.idCategoria IN :categoriasIds
                AND p.idProducto != :productoId
                GROUP BY p
                ORDER BY COUNT(c) DESC
            """)
    List<Producto> findRelatedProducts(
            @Param("categoriasIds") List<Integer> categoriasIds,
            @Param("productoId") Integer productoId,
            Pageable pageable);
}
