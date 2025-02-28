package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "imgProd")
public class ImgProd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idImg;
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
}
