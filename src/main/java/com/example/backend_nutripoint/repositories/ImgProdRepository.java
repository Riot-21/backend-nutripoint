package com.example.backend_nutripoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.ImgProd;

public interface ImgProdRepository extends JpaRepository<ImgProd, Integer>{
    List<ImgProd> findByProductoIdProducto(Integer idProducto);
}
