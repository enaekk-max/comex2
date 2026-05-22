package com.aduanas.comex.clasificacionms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EvaluarClasificacionRequestDTO {

    @NotNull(message = "El ID de la carga es obligatorio")
    private Long cargaId;

    @NotBlank (message = "El RUT del importador es obligatorio")
    private String importadorRut;

    @NotBlank(message = "La descripción de la mercancía es obligatoria")
    private String descripcionMercancia;

    @NotBlank(message = "El país de origen es obligatorio")
    private String paisOrigen;

    @NotNull(message = "El valor declarado es obligatorio")
    @Positive(message = "El valor declarado debe ser mayor a cero")
    private BigDecimal valorDeclarado;



}