package com.example.backend_nutripoint.controllers;

import com.example.backend_nutripoint.services.SeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seed")
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping
    public ResponseEntity<String> seedDatabase() {
        try {
            seedService.seedDatabase();
            return ResponseEntity.ok("Base de datos poblada exitosamente con datos por defecto.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error poblando la base de datos: " + e.getMessage());
        }
    }
}
