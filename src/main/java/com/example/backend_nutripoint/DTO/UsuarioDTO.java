package com.example.backend_nutripoint.DTO;

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
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 8, message = "El numero de DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank
    @Size(min = 9, max = 9, message = "El numero de telefono debe tener 9 digitos")
    private String telefono;
}
