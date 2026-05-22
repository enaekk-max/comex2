package com.aduanas.com.documentosms.repository;

import com.aduanas.com.documentosms.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // ✅ AGREGA ESTO: Para que Spring lo detecte como un Bean de persistencia
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Listo. Todos los métodos CRUD ya están mapeados por defecto.
}