package com.aduanas.com.pagosms.entity;

import com.aduanas.com.pagosms.Enum.EstadoPago; // ✅ CORREGIDO: Paquete en minúscula y plural
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="pagos")


public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago; // ✅ CORREGIDO: Estándar unificado idAlInicio

    @Column(name = "id_cargas", nullable = false)
    private Long idCarga; // ✅ CORREGIDO: Estándar unificado idAlInicio

    /*
     * ¡BigDecimal: El rey para precios, impuestos y saldos!
     * precision = 10: Número total de dígitos.
     * scale = 2: Dígitos destinados a los centavos.
     */
    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda; // Código ISO (USD, CLP, MXN)

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago")
    private EstadoPago estadoPago; // Otro Enum para controlar los estados

    @Column(name = "id_transaccion_externa", unique = true) // ✅ CORREGIDO: Nombre unificado
    private String idTransaccionExterna;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago; // LocalDateTime es el estándar para marcas de tiempo


}

