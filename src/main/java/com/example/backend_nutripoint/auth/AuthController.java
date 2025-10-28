package com.example.backend_nutripoint.auth;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.models.Role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, List.of(Role.USER)));
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        if(request.getRoles().contains(Role.USER) && !request.getRoles().contains(Role.SUPER_ADMIN)){
            return ResponseEntity.ok(authService.register(request, List.of(Role.ADMIN)));
        }
        return ResponseEntity.ok(authService.register(request, request.getRoles()));
    }

}
