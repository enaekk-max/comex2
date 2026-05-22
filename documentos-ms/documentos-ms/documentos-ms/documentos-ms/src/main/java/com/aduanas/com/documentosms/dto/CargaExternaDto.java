package com.aduanas.com.documentosms.dto;

import lombok.Data;

@Data
public class CargaExternaDto {
    private Long idCarga; // ✅ CORREGIDO: Matamos el "id" genérico
    private String estado;
}