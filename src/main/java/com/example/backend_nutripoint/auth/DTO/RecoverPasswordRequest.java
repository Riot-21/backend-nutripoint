package com.example.backend_nutripoint.auth.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoverPasswordRequest {
    @NotBlank
    @Email
    private String email;
}
