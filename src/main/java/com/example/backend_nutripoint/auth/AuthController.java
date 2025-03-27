package com.example.backend_nutripoint.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_nutripoint.models.Role;
import com.example.backend_nutripoint.repositories.UsuarioRepository;

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
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/otro")
    public String hola() {
        return "hola";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        System.out.println("endpoint login");

        if (result.hasErrors()) {
            return validation(result);
        }
        try {
            return ResponseEntity.ok(authService.login(request, Role.USER));
        } catch (Exception ex) {
            System.out.println("There is an exception: ");
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Bad email or password");
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest request, BindingResult result) {
        System.out.println("endpoint login");

        if (result.hasErrors()) {
            return validation(result);
        }
        try {
            return ResponseEntity.ok(authService.login(request, Role.ADMIN));
        } catch (Exception ex) {
            System.out.println("There is an exception: ");
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Bad email or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        System.out.println("endpoint register");
        if (result.hasErrors()) {
            System.out.println("errores validation");
            return validation(result);
        }
        try {

            if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Email address already used");
            }else if (usuarioRepository.findByDni(request.getDni()).isPresent()){
                return ResponseEntity.badRequest().body("Dni number is already resgistrated");

            }
            return ResponseEntity.ok(authService.registerUser(request));

        } catch (Exception ex) {
            System.out.println("there is an exception: ");
            ex.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Error: no se puede ejecutar");
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        System.out.println("endpoint register");
        if (result.hasErrors()) {
            System.out.println("errores validation");
            return validation(result);
        }
        try {

            if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Email address already used");
            }else if (usuarioRepository.findByDni(request.getDni()).isPresent()){
                return ResponseEntity.badRequest().body("Dni number is already resgistrated");

            }
            return ResponseEntity.ok(authService.registerAdmin(request));

        } catch (Exception ex) {
            System.out.println("there is an exception: ");
            ex.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Error: no se puede ejecutar");
    }
    


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
