package com.aduanas.comex.riesgo_ms.repository;

import com.aduanas.comex.riesgo_ms.entity.Riesgo;
import com.aduanas.comex.riesgo_ms.enums.CanalRiesgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

    List<Riesgo> findByCanalAsignado(CanalRiesgo canal);

    // ✅ CORREGIDO: Cambiado findByCargaId por findByIdCarga para alinearse a la Entidad
    List<Riesgo> findByIdCarga(Long idCarga);
}