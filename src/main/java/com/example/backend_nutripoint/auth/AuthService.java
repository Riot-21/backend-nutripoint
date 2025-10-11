package com.example.backend_nutripoint.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.jwt.JwtService;
import com.example.backend_nutripoint.models.Role;
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

    @Transactional
    public AuthResponse login(LoginRequest request, Role expectedRole) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        Usuario user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole()!= expectedRole ){
            throw new RuntimeException("Acceso denegado, no tienes permisospara esta seccion");
        }

        String token = jwtService.createJwtToken(user);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getIdUsuario())
                .build();

    }

    @Transactional
    public AuthResponse register(RegisterRequest request, Role role) {
        // Validar que el rol sea válido (el parámetro ya es un Enum, así que no
        // necesitas toUpperCase())
        if (role != Role.USER && role != Role.ADMIN) {
            throw new RuntimeException("Rol inválido. Debe ser USER o ADMIN.");
        }
        Usuario user = new Usuario();
        user.setNombres(request.getNombres());
        user.setApellidos(request.getApellidos());
        user.setEmail(request.getEmail());
        user.setDni(request.getDni());
        user.setTelefono(request.getTelefono());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEstado(true);
        user.setRole(role);

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.createJwtToken(user))
                .userId(user.getIdUsuario())
                .build();
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request){
        return register(request, Role.USER);
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse registerAdmin(RegisterRequest request){
        return register(request, Role.ADMIN);
    }
}
