package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
// @AllArgsConstructor
@Table(name = "tiendas")
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTienda;
    private String distrito;
    private String direccion;
    private Integer telefono;
    private String horario;
    private String foto;
}
