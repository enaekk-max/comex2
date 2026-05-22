CREATE TABLE clasificaciones (
    id_clasificacion BIGINT PRIMARY KEY AUTO_INCREMENT, -- ✅ Id clara y estandarizada
    id_carga BIGINT NOT NULL,                          -- ✅ Sincronizado con el id_carga de tu otro microservicio
    tipo_clasificacion VARCHAR(50) NOT NULL,
    permitido BOOLEAN NOT NULL,
    monto_impuesto DECIMAL(15,2) NOT NULL,
    observaciones VARCHAR(500),
    fecha_evaluacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP --
);