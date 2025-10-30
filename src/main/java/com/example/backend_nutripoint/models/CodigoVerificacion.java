package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
// @AllArgsConstructor
@Table(name = "codigo_verificacion")
public class CodigoVerificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String codigo;
    private Date fechaCreacion;
    private Date fechaExpiracion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
