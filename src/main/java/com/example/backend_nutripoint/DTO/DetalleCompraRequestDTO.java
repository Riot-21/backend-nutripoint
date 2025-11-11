package com.example.backend_nutripoint.DTO;

// import java.math.BigDecimal;


// import jakarta.validation.constraints.DecimalMin;
// import jakarta.validation.constraints.Digits;
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

    // @NotNull(message = "El precio es obligatorio")
    // @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    // @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 dígitos enteros y 2 decimales")
    // private BigDecimal precioUni;

    // @NotNull(message = "El subtotal es obligatorio")
    // @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    // @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 dígitos enteros y 2 decimales")
    // private BigDecimal subtotal;

    @NotNull
    private Integer idProducto;

    // private Compra compra;
}
