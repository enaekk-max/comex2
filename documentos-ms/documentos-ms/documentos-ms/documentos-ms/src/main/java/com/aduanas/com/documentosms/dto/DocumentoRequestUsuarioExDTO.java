package com.aduanas.com.documentosms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoRequestUsuarioExDTO {

    @NotNull(message = "El ID de la carga es obligatorio")
    private Long cargaId;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observacionManual;
}