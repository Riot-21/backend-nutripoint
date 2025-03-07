package com.example.backend_nutripoint.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotBlank
    @Column(nullable = false)
    private String nombres;

    @NotBlank
    @Column(nullable = false)
    private String apellidos;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(min = 8, max = 8, message = "El numero de DNI debe tener 8 dígitos")
    @Column(unique = true, nullable = false)
    private String dni;

    @NotBlank
    @Size(min = 9, max = 9, message = "El numero de telefono debe tener 9 digitos")
    @Column(nullable = false)
    private String telefono;
}
