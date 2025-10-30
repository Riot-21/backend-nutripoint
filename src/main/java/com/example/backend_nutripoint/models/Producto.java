package com.example.backend_nutripoint.models;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
public class Producto {
    //!DIFERENCIA ENTRE INTGER E INT: INTEGER ES MEJOR PARA ID PORQUE ES NECESARIO QUE PERMITA NULL ANTES DE SER CREADO EN BD
    //!INT DEFINE SIEMPRE CON 0, POR LO QUE SIEMPRE OBLIGA A QUE HAYA UN VALOR
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

    @Column(nullable = false, precision = 10, scale = 2)
    // private Double precioUnit;
    private BigDecimal precioUnit;

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
