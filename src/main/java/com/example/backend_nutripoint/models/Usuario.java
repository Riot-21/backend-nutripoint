package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
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

    private String nombres;
    private String apellidos;

    @Column(unique = true)
    private String email;
    private String contraseña;
    private Integer dni;
    private Integer telefono;
    private String estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Compra> compras;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Token> tokens;
}
