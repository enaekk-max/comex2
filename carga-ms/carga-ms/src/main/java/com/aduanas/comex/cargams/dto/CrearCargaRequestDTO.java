package com.aduanas.comex.cargams.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearCargaRequestDTO {

    // ✅ CORREGIDO: Renombrado a 'nroDeclaracion' para que calce idéntico con tu Entidad
    @NotBlank(message = "El número de declaración es obligatorio")
    private String numeroDeclaracion;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotBlank(message = "El país de origen es obligatorio")
    private String paisOrigen;

    @NotNull(message = "El valor declarado es obligatorio")
    @Positive(message = "El valor declarado debe ser mayor a cero")
    private BigDecimal valorDeclarado;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a cero")
    private BigDecimal peso;

    @NotBlank(message = "El RUT del importador es obligatorio")
    private String importadorRut;
}