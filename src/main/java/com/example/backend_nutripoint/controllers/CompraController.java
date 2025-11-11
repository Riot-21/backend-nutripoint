package com.example.backend_nutripoint.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.DTO.CompraRequestDTO;
import com.example.backend_nutripoint.DTO.CompraResponseDTO;
import com.example.backend_nutripoint.services.CompraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/compra")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    @GetMapping("/otro")
    public String getMethodName() {
        return "hola ps";
    }
    

    @PostMapping
    public ResponseEntity<CompraResponseDTO> realizarCompra(@Valid @RequestBody CompraRequestDTO dto, Authentication auth) {
        CompraResponseDTO compra = compraService.realizarCompra(dto, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(compra);
    }
    
}
