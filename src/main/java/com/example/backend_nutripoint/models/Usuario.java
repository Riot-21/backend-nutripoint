package com.example.backend_nutripoint.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Boolean estado = true;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Compra> compras;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private List<Role> roles;

}
