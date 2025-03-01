package com.example.backend_nutripoint.auth;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend_nutripoint.jwt.JwtService;
import com.example.backend_nutripoint.models.Usuario;
import com.example.backend_nutripoint.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(LoginRequest request){
        authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword()));
    Usuario user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));
    
        String token = jwtService.createJwtToken(user);

        return AuthResponse.builder()
            .token(token)
            .userId(user.getId_usuario())
            .build();

    }

    @Transactional
    public AuthResponse register(RegisterRequest request){
        Usuario user = new Usuario();
        user.setNombres(request.getNombres());
        user.setApellidos(request.getApellidos());
        user.setEmail(request.getEmail());
        user.setDni(request.getDni());
        user.setTelefono(request.getTelefono());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEstado(true);

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.createJwtToken(user))
                .userId(user.getId_usuario())
                .build();
    }
}
