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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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

    // public AuthResponse register(RegisterRequest request){
    //     // if(userRepository.exists(request.getEmail())){
    //     //     throw new RuntimeException("User already exists")
    //     // }
    // }
}
