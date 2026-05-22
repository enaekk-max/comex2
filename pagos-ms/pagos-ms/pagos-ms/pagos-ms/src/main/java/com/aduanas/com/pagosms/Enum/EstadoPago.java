package com.aduanas.com.pagosms.Enum;

public enum EstadoPago {

    PENDIENTE,   // El usuario aún no paga
    PROCESANDO,  // El pago está en validación (ej. transferencia bancaria)
    COMPLETADO,  // ¡Plata en mano!
    FALLIDO,     // La tarjeta rebotó o hubo error

}
