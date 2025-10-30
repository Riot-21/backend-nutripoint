package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}
