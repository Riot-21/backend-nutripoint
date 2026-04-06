package com.example.backend_nutripoint.auth.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleAuthRequest {
    @NotBlank
    private String token;
}
