
CREATE TABLE evaluaciones_riesgo (
id_evaluacion_riesgo BIGINT AUTO_INCREMENT PRIMARY KEY,         -- ✅ UNIFICADO
    id_carga BIGINT NOT NULL,                                        -- ✅ UNIFICADO con tu ecosistema
    puntaje_riesgo INT NOT NULL,
    canal_asignado VARCHAR(20) NOT NULL,
    motivo_alerta TEXT,
    evaluado_por VARCHAR(100) DEFAULT 'SISTEMA_AUTOMATICO',
    fecha_evaluacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- ✅ Asegurado contra nulos

CONSTRAINT uq_carga_riesgo UNIQUE (id_carga)                    -- ✅ Columna unificada en el UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ✅ Índice unificado con el nombre de columna correcto
CREATE INDEX idx_riesgo_id_carga ON evaluaciones_riesgo(id_carga);