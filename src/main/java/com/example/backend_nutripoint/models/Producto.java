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
    private Integer idProducto;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private Double precioUnit;

    @Column(nullable = false)
    private String modEmpleo;

    @Column(nullable = false)
    private String advert;

    @ManyToMany
    @JoinTable(
            name = "prod_categoria",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ImgProd> imagenes;

}
