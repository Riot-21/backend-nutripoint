package com.example.backend_nutripoint.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalleCompra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    private Integer item;
    private Integer cantidad;
    private Double preciouni;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compra compra;
}
