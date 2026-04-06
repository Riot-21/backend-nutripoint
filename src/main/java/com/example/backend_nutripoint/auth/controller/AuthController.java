package com.example.backend_nutripoint.auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.DTO.responses.MessageResponseDTO;
import com.example.backend_nutripoint.auth.DTO.AuthResponse;
import com.example.backend_nutripoint.auth.DTO.GoogleAuthRequest;
import com.example.backend_nutripoint.auth.DTO.LoginRequest;
import com.example.backend_nutripoint.auth.DTO.RecoverPasswordRequest;
import com.example.backend_nutripoint.auth.DTO.RegisterRequest;
import com.example.backend_nutripoint.auth.DTO.ResetPasswordRequest;
import com.example.backend_nutripoint.auth.services.AuthService;
import com.example.backend_nutripoint.models.Role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/otro")
    // @PreAuthorize("hasRole('ADMIN')")
    public String hola() {
        return "hola";
    }

    @GetMapping("/hola")
    @PreAuthorize("hasRole('ADMIN')")
    public String hola2() {
        return "hola";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, List.of(Role.USER)));
    }

    @PostMapping("/refresh-token")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> refresgToken(Authentication auth) {
        // System.out.println("HOLAAAAA"+auth.getPrincipal());
        return ResponseEntity.ok(authService.refreshToken(auth.getName()));
    }
    

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        if(request.getRoles().contains(Role.USER) && !request.getRoles().contains(Role.SUPER_ADMIN)){
            return ResponseEntity.ok(authService.register(request, List.of(Role.ADMIN)));
        }
        return ResponseEntity.ok(authService.register(request, request.getRoles()));
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> loginGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        return ResponseEntity.ok(authService.loginWithGoogle(request));
    }

    @PostMapping("/google-register")
    public ResponseEntity<AuthResponse> registerGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        return ResponseEntity.ok(authService.registerWithGoogle(request));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<MessageResponseDTO> recoverPassword(@Valid @RequestBody RecoverPasswordRequest request) {
        authService.recoverPassword(request.getEmail());
        return ResponseEntity.ok(new MessageResponseDTO("Código de recuperación enviado al correo."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO("Contraseña restablecida exitosamente."));
    }

}
