package com.example.backend_nutripoint.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.requests.CompraRequestDTO;
import com.example.backend_nutripoint.DTO.requests.DetalleCompraRequestDTO;
import com.example.backend_nutripoint.DTO.responses.CompraResponseDTO;
import com.example.backend_nutripoint.auth.services.EmailService;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.mappers.CompraMapper;
import com.example.backend_nutripoint.models.Compra;
import com.example.backend_nutripoint.models.DetalleCompra;
import com.example.backend_nutripoint.models.EstadoCompra;
import com.example.backend_nutripoint.models.Producto;
import com.example.backend_nutripoint.models.Usuario;
import com.example.backend_nutripoint.repositories.CompraRepository;
import com.example.backend_nutripoint.repositories.ProductoRepository;
import com.example.backend_nutripoint.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompraService {
        private final CompraRepository compraRepository;
        private final UsuarioRepository usuarioRepository;
        private final ProductoRepository productoRepository;
        private final EmailService emailService;

        @Transactional
        public CompraResponseDTO realizarCompra(CompraRequestDTO dto, String emailUser) {
                Usuario usuario = usuarioRepository.findByEmail(emailUser)
                                .orElseThrow(() -> new NotFoundException("user not found"));

                Compra compra = new Compra();
                compra.setUsuario(usuario);
                compra.setCodigoCompra(generateCode());
                compra.setFecha(LocalDateTime.now());
                compra.setDireccion(dto.getDireccion());
                compra.setDistrito(dto.getDistrito());
                compra.setEstado(EstadoCompra.PAGADO);
                compra.setTipoPago(dto.getTipoPago());

                List<DetalleCompra> detalles = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                for (DetalleCompraRequestDTO detalleDTO : dto.getDetalles()) {
                        Producto prod = productoRepository.findById(detalleDTO.getIdProducto())
                                        .orElseThrow(() -> new NotFoundException("product not found"));

                        if (prod.getStock() < detalleDTO.getCantidad()) {
                                throw new IllegalArgumentException("Stock insuficiente, producto: " + prod.getNombre()
                                                + ", stock: " + prod.getStock());
                        }

                        prod.setStock(prod.getStock() - detalleDTO.getCantidad());
                        productoRepository.save(prod);

                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setProducto(prod);
                        detalle.setCompra(compra);
                        detalle.setCantidad(detalleDTO.getCantidad());
                        detalle.setPrecioUni(prod.getPrecioUnit());
                        System.out.println(prod.getPrecioUnit());
                        BigDecimal subtotal = prod.getPrecioUnit()
                                        .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
                        detalle.setSubtotal(subtotal);

                        total = total.add(subtotal);
                        detalles.add(detalle);
                }

                compra.setTotal(total);
                compra.setDetalles(detalles);

                // tipo cascade guarda detalle automaticamente
                Compra savedCompra = compraRepository.save(compra);
                emailService.enviarEmailCompra(savedCompra);

                return CompraMapper.compraToDTO(savedCompra);
        }

        private String generateCode() {
            String random = UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();

            return "COMP-" + random;
        }

        // private CompraResponseDTO mapToDTO(Compra compra) {
        //         return CompraResponseDTO.builder()
        //                         .idCompra(compra.getIdCompra())
        //                         .idUsuario(compra.getUsuario().getIdUsuario())
        //                         .fecha(compra.getFecha())
        //                         .total(compra.getTotal())
        //                         .tipoPago(compra.getTipoPago())
        //                         .direccion(compra.getDireccion())
        //                         .distrito(compra.getDistrito())
        //                         .estadoCompra(compra.getEstado())
        //                         .detalle(compra.getDetalles().stream()
        //                                         .map(d -> DetalleCompraResponseDTO.builder()
        //                                                         .idDetalle(d.getIdDetalle())
        //                                                         .idProducto(d.getProducto().getIdProducto())
        //                                                         .nombreProd(d.getProducto().getNombre())
        //                                                         .cantidad(d.getCantidad())
        //                                                         .precioUnit(d.getPrecioUni())
        //                                                         .subtotal(d.getSubtotal())
        //                                                         .build())
        //                                         .toList())
        //                         .build();

        // }

}
