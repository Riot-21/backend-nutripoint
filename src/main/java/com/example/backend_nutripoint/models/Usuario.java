package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

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
    @Size(min = 8, max = 64)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 8, max = 8, message = "El telefono debe tener 9 dígitos")
    @Column(unique = true, nullable = false)
    private String dni;

    @NotBlank
    @Size(min = 9, max = 9, message = "El telefono debe tener 9 dígitos")
    @Column(nullable = false)
    private String telefono;

    @NotNull
    private Boolean estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Compra> compras;

}
