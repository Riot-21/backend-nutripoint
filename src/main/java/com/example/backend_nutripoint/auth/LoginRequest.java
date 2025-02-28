package com.example.backend_nutripoint.auth;

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
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;
}
