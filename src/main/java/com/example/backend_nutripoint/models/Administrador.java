package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
// @AllArgsConstructor
@Table(name = "administrador")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAdmin;
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private Integer dni;
    private Integer telefono;
    private String estado;
}
