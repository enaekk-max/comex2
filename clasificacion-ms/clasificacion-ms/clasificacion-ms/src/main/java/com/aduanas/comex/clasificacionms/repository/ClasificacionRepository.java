package com.aduanas.comex.clasificacionms.repository;

import com.aduanas.comex.clasificacionms.entity.Clasificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long> {

    // ✅ SOLUCIONADO: Estructura exacta para que Spring Data encuentre "idClasificacion" sin romperse
    Optional<Clasificacion> findFirstByCargaIdOrderByIdClasificacionDesc(Long cargaId);
}