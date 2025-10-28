package com.example.backend_nutripoint.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.exceptions.UserNotFoundException;
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
    public AuthResponse login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        Usuario user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, List<Role> requestedRoles) {
        validateUniqueUser(request);
        List<Role> roles = sanitizeRoles(requestedRoles);

        Usuario user = buildNewUser(request, roles);
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(Usuario user) {
        return AuthResponse.builder()
                .token(jwtService.createJwtToken(user))
                .userId(user.getIdUsuario())
                .roles(user.getRoles().stream().map(Enum::name).toList())
                .build();
    }

    private List<Role> sanitizeRoles(List<Role> requestedRoles) {
        if (requestedRoles == null || requestedRoles.isEmpty()) {
            return List.of(Role.USER);
        }

        Set<Role> validRoles = new HashSet<>();
        for (Role role : requestedRoles) {
            if (role == Role.USER || role == Role.ADMIN || role == Role.SUPER_ADMIN) {
                validRoles.add(role);
            } else {
                throw new IllegalArgumentException("Rol inválido: " + role);
            }
        }
        if (validRoles.contains(Role.SUPER_ADMIN)) {
            validRoles.add(Role.ADMIN);
        }
        validRoles.add(Role.USER);

        return List.copyOf(validRoles);
    }

    private void validateUniqueUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (userRepository.existsByDni(request.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }
    }

    private Usuario buildNewUser(RegisterRequest request, List<Role> roles) {
        Usuario user = new Usuario();
        user.setNombres(request.getNombres());
        user.setApellidos(request.getApellidos());
        user.setEmail(request.getEmail());
        user.setDni(request.getDni());
        user.setTelefono(request.getTelefono());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEstado(true);
        user.setRoles(roles);
        return user;
    }

}
