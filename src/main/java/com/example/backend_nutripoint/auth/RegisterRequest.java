package com.example.backend_nutripoint.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class RegisterRequest {
    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,64}$", message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;

    @NotBlank
    @Size(min = 9, max = 9)
    private String telefono;
}
