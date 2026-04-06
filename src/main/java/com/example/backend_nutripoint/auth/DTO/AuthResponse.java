package com.example.backend_nutripoint.auth.DTO;


import com.example.backend_nutripoint.DTO.responses.UsuarioResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UsuarioResponseDTO user;
}
