package com.aduanas.comex.notificacion_ms.exception;

public class NotificacionException extends RuntimeException {

    // Constructor básico para mensajes personalizados
    public NotificacionException(String mensaje) {
        super(mensaje);
    }

    // ✅ RECOMENDADO: Constructor para arrastrar el error original (ej: InterruptedException, IllegalArgumentException)
    public NotificacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}