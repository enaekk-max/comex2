package com.aduanas.com.documentosms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoResponseAnalistaDTO {
    private Long idDocumento;
    private String estadoValidacion;
    private String datosExtraidos;
    private LocalDateTime fechaRevision;
}