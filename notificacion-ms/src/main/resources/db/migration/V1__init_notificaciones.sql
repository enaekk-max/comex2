-- Crear tabla para el historial de alertas y notificaciones enviadas
CREATE TABLE notificaciones (
id_notificacion BIGINT AUTO_INCREMENT PRIMARY KEY,                -- ✅ UNIFICADO
    id_carga BIGINT NOT NULL,                                         -- ✅ UNIFICADO con tu ecosistema
    destinatario VARCHAR(150) NOT NULL,
    tipo_canal VARCHAR(30) DEFAULT 'EMAIL',
    asunto VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    estado_envio VARCHAR(30) NOT NULL,                                -- 'PENDIENTE', 'ENVIADO', 'FALLIDO'
    fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,         -- ✅ Asegurado contra nulos

   CONSTRAINT chk_destinatario_notif CHECK (destinatario LIKE '%@%')
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ✅ Índice corregido con el nombre de columna unificado
CREATE INDEX idx_notificaciones_id_carga ON notificaciones(id_carga);