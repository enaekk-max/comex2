CREATE TABLE pagos (
    id_pago BIGINT PRIMARY KEY AUTO_INCREMENT,        -- ✅ UNIFICADO: De 'pago_id' a 'id_pago'
    id_carga BIGINT NOT NULL,                        -- ✅ UNIFICADO: De 'carga_id' a 'id_carga' (Igual a los otros dos servicios)
    monto DECIMAL(15,2) NOT NULL,                    -- ✅ MEJORADO: DECIMAL(15,2) por si llegan cargas con montos gigantes en CLP
    moneda VARCHAR(3) NOT NULL,                      -- Código ISO (ej: 'CLP', 'USD')
    estado_pago VARCHAR(30) NOT NULL,                -- 'PENDIENTE', 'PROCESANDO', 'COMPLETADO', 'FALLIDO'
    transaccion_externa_id VARCHAR(100) UNIQUE,      -- El váucher único para evitar duplicados
    fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- ✅ Asegurado con el DEFAULT para evitar nulos desde Java
);