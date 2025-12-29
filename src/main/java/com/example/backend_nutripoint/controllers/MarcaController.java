package com.example.backend_nutripoint.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.DTO.CreateMarcaDTO;
import com.example.backend_nutripoint.models.Marca;
import com.example.backend_nutripoint.services.MarcaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marca")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(marcaService.getAllBrands());
    }
    
    @PostMapping
    public ResponseEntity<Marca> createCategory(@RequestBody CreateMarcaDTO marca) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.createBrand(marca));
    }


}
