package com.example.backend_nutripoint.mappers;

import com.example.backend_nutripoint.DTO.responses.UsuarioResponseDTO;
import com.example.backend_nutripoint.models.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static UsuarioResponseDTO userToDTO(Usuario user) {
        return UsuarioResponseDTO.builder()
                .idUsuario(user.getIdUsuario())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .email(user.getEmail())
                .dni(user.getDni())
                .telefono(user.getTelefono())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.toString())
                        .toList())
                .build();
    }
}

