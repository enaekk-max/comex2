package com.aduanas.comex.clasificacionms.entity;

import com.aduanas.comex.clasificacionms.enums.TipoClasificacion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clasificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clasificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion") // ✅ CORREGIDO: Nombre de columna explícito en MySQL
    private Long idClasificacion;      // ✅ CORREGIDO: Variable renombrada para matar el "id" a secas

    @Column(name = "id_Carga", nullable = false)
    private Long idCarga; // Excelente, este amarra perfecto con el microservicio de Carga

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_clasificacion", nullable = false)
    private TipoClasificacion tipoClasificacion;

    @Column(name = "permitido", nullable = false) // ✅ CORREGIDO: Nombre explícito
    private Boolean permitido;

    @Column(name = "monto_impuesto", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoImpuesto;

    @Column(name = "observaciones", length = 500) // ✅ CORREGIDO: Nombre explícito
    private String observaciones;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;


}