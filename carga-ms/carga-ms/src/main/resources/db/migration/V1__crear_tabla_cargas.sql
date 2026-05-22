CREATE TABLE cargas (
    id_carga BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_declaracion VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    pais_origen VARCHAR(100) NOT NULL,
    valor_declarado DECIMAL(15,2) NOT NULL,
    peso DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    importador_rut VARCHAR(20) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto_impuesto DECIMAL(15,2) NULL,
    permitido TINYINT(1) NULL,
    observaciones VARCHAR(255) NULL
);