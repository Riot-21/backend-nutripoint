package com.example.backend_nutripoint.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleCompraRequestDTO {

    @NotNull(message = "es obligatorio")
    @Min(value = 1, message = "El stock debe ser mayor a 0")
    private int cantidad;

    @NotNull
    private Integer idProducto;

}
