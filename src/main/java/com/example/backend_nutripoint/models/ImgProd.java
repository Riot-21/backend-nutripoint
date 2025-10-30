package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
@Table(name = "img_prod")
public class ImgProd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idImg;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}
