package com.example.backend_nutripoint.DTO;

import java.util.List;

import com.example.backend_nutripoint.models.TipoPago;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraRequestDTO {

    @NotNull
    private TipoPago tipoPago;

    @NotBlank
    private String direccion;
    
    @NotBlank
    private String distrito;

    @NotEmpty(message = "debe haber detalle de compra")
    private List<@Valid DetalleCompraRequestDTO> detalles;
}
