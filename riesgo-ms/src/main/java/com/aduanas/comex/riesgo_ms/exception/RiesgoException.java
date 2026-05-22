package com.aduanas.comex.riesgo_ms.exception;

// ======================================================
// ================= RIESGO EXCEPTION ===================
// ======================================================
//
// Esta es una excepción personalizada.
// Sirve para lanzar errores propios del microservicio.
//
public class RiesgoException extends RuntimeException {

    // ======================================================
    // ================= CONSTRUCTORES ======================
    // ======================================================

    // Constructor básico para mensajes personalizados de negocio
    public RiesgoException(String mensaje) {
        super(mensaje);
    }

    // ✅ AGREGADO: Permite anidar la causa real del fallo (Exception Chaining)
    // Esencial para no perder el detalle técnico del error en los logs de la consola.
    public RiesgoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}