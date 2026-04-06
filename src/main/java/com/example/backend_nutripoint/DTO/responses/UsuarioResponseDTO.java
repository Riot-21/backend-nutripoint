package com.example.backend_nutripoint.DTO.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private List<String> roles;
}
