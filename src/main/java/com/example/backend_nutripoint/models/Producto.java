package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    private String nombre;
    private String descripcion;
    private Integer stock;
    private String marca;
    private Double preciounit;
    private String mod_empleo;
    private String advert;

    @ManyToMany
    @JoinTable(
            name = "prodCategoria",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ImgProd> imagenes;

}
