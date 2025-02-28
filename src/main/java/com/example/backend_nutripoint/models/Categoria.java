package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_categoria;

    private String categoria;
    private String objetivo;

    @ManyToMany(mappedBy = "categorias")
    private List<Producto> productos;
}
