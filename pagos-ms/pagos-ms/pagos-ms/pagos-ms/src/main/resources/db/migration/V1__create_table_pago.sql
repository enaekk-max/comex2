CREATE TABLE pagos (
       id_pago BIGINT PRIMARY KEY AUTO_INCREMENT,        -- ✅ UNIFICADO: De 'pago_id' a 'id_pago'
       id_carga BIGINT NOT NULL,                        -- ✅ UNIFICADO: De 'carga_id' a 'id_carga'
       monto DECIMAL(15,2) NOT NULL,                    -- ✅ MEJORADO: Por si entran montos gigantes en CLP
       moneda VARCHAR(3) NOT NULL,                      -- Código ISO (ej: 'CLP', 'USD')
       estado_pago VARCHAR(30) NOT NULL,                -- 'PENDIENTE', 'PROCESANDO', 'COMPLETADO' o 'FALLIDO'
       transaccion_externa_id VARCHAR(100) UNIQUE,      -- El váucher manual de Postman
       fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- ✅ Asegurado contra nulos
   );
