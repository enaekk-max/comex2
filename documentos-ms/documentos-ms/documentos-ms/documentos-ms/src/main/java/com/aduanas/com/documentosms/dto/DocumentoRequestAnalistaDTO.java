package com.aduanas.com.documentosms.dto;

import com.aduanas.com.documentosms.Enum.EstadoValidacionArchivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoRequestAnalistaDTO {

    @NotNull(message = "El ID del documento es obligatorio")
    private Long idDocumento; // ✅ CORREGIDO: Nombre unificado

    @NotNull(message = "Debes seleccionar el resultado de la revisión")
    private EstadoValidacionArchivo resultadoRevision;

    @NotBlank(message = "Las observaciones de la revisión son obligatorias")
    private String observaciones;
}