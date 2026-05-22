package com.aduanas.com.documentosms.entity;

import com.aduanas.com.documentosms.Enum.EstadoValidacion;
import com.aduanas.com.documentosms.Enum.EstadoValidacionArchivo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documentos") // ✅ Sugerencia: pluralizado para seguir el estándar de 'clasificaciones' y 'cargas'
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento; // ✅ CORREGIDO: Renombrado a idDocumento para seguir el estándar de idCarga e idClasificacion

    // ✅ CORREGIDO: Cambiado de 'cargaId' a 'idCarga' para habilitar .idCarga() en el Builder
    @Column(name = "id_carga", nullable = false)
    private Long idCarga;

    @Column(name = "tipo_documento", nullable = false, length = 100) // ✅ Especificado
    private String tipoDocumento;

    @Column(name = "ruta_archivo", nullable = false, length = 255)
    private String rutaArchivo;

    @Column(name = "observacion_manual", length = 500)
    private String observacionManual;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_revision", length = 50)
    private EstadoValidacionArchivo resultadoRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_validacion", nullable = false, length = 50)
    private EstadoValidacion estadoValidacion;

    @Column(name = "fecha_documento", nullable = false) // ✅ CORREGIDO: En singular para coincidir con la variable
    private LocalDateTime fechaDocumento;


}