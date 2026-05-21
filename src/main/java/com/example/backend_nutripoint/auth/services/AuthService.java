package com.example.backend_nutripoint.auth.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.auth.DTO.AuthResponse;
import com.example.backend_nutripoint.auth.DTO.GoogleAuthRequest;
import com.example.backend_nutripoint.auth.DTO.LoginRequest;
import com.example.backend_nutripoint.auth.DTO.RegisterRequest;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.jwt.JwtService;
import com.example.backend_nutripoint.mappers.UsuarioMapper;
import com.example.backend_nutripoint.models.AuthProvider;
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
    private final EmailService emailService;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Usuario user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getProvider() == AuthProvider.GOOGLE) {
            throw new IllegalArgumentException("Esta cuenta usa login con Google");
        }

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, Role registerType) {
        validateUniqueUser(request);
        List<Role> roles = sanitizeRoles(registerType);

        Usuario user = buildNewUser(request, roles);
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse refreshToken(String email) {
        Usuario u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user not found" ));
        ;
        return buildAuthResponse(u);
    }

    private AuthResponse buildAuthResponse(Usuario user) {
        return AuthResponse.builder()
                .token(jwtService.createJwtToken(user))
                .user(UsuarioMapper.userToDTO(user))
                .build();
    }

    private List<Role> sanitizeRoles(Role registerType) {
        Set<Role> validRoles = new HashSet<>();
            if(registerType == Role.USER){
                validRoles.add(Role.USER);
            }
            if(registerType == Role.ADMIN){
                validRoles.add(Role.ADMIN);
                validRoles.add(Role.USER);
            }
            if(registerType == Role.SUPER_ADMIN){
                validRoles.add(Role.SUPER_ADMIN);
                validRoles.add(Role.ADMIN);
                validRoles.add(Role.USER);
            }

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
        user.setProvider(AuthProvider.LOCAL);
        return user;
    }

    @Transactional
    public AuthResponse loginWithGoogle(GoogleAuthRequest request) {
        try {
            var payload = googleTokenVerifier.verify(request.getToken());
            String email = payload.getEmail();
            Usuario user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Usuario no registrado. Por favor, regístrese primero."));
            return buildAuthResponse(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Token de Google inválido o error en autenticación: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse registerWithGoogle(GoogleAuthRequest request) {
        try {
            var payload = googleTokenVerifier.verify(request.getToken());
            String email = payload.getEmail();

            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("El email ya está registrado");
            }

            Usuario user = new Usuario();
            user.setNombres((String) payload.get("given_name"));
            user.setApellidos((String) payload.get("family_name"));
            user.setEmail(email);
            user.setDni(null);
            user.setTelefono(null);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setEstado(true);
            user.setRoles(List.of(Role.USER));
            user.setProvider(AuthProvider.GOOGLE);

            userRepository.save(user);

            return buildAuthResponse(user);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error en registro con Google: " + e.getMessage());
        }
    }

    @Transactional
    public void recoverPassword(String email) {
        // !revisar los usernotfound ya que por reglas de negocio puede ser perjudicial
        // revelar usuarios
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (user.getProvider() == AuthProvider.GOOGLE) {
            throw new IllegalArgumentException("Esta cuenta usa login con Google");
        }

        if (user.getRecoveryCode() != null &&
                user.getRecoveryCodeExpiration() != null &&
                user.getRecoveryCodeExpiration().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Ya existe un código de recuperación activo. Por favor, espere a que expire para solicitar uno nuevo.");
        }

        String code = String.format("%06d", new java.util.Random().nextInt(999999));

        user.setRecoveryCode(passwordEncoder.encode(code));
        user.setRecoveryCodeExpiration(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendCodeRecoveryPassword(email, code);

    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (user.getRecoveryCode() == null || user.getRecoveryCodeExpiration() == null) {
            throw new IllegalArgumentException("No hay cödigo de recuperación activo");
        }

        if (user.getRecoveryCodeExpiration().isBefore(LocalDateTime.now())) {
            user.setRecoveryCode(null);
            user.setRecoveryCodeExpiration(null);
            userRepository.save(user);

            throw new IllegalArgumentException("El código ha expirado");
        }

        if (!passwordEncoder.matches(code, user.getRecoveryCode())) {
            throw new IllegalArgumentException("Código inválido");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setRecoveryCode(null);
        user.setRecoveryCodeExpiration(null);
        userRepository.save(user);
    }
}
