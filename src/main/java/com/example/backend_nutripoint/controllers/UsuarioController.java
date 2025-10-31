package com.example.backend_nutripoint.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_nutripoint.DTO.UsuarioDTO;
import com.example.backend_nutripoint.models.Usuario;
import com.example.backend_nutripoint.repositories.UsuarioRepository;
import com.example.backend_nutripoint.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService userService;
    private final UsuarioRepository usuarioRepository;
    
    @GetMapping("/profile")
    public ResponseEntity<UsuarioDTO> verPerfil(Authentication auth) {
        UsuarioDTO user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/otro")
    public String hola() {
        return "hola";
    }
    

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication auth, @Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Usuario user = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return ResponseEntity.ok(userService.updateUser(user.getIdUsuario(), usuarioDTO));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
