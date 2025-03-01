package com.example.backend_nutripoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_nutripoint.models.Usuario;
import java.util.Optional;
import java.util.List;



public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
}
