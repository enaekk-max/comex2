package com.aduanas.comex.cargams.repository;

import com.aduanas.comex.cargams.entity.Carga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CargaRepository extends JpaRepository<Carga, Long> {

    // ✅ AGREGADO: Te permite buscar una carga específica por su número de tramitación aduanera
    Optional<Carga> findByNumeroDeclaracion(String numeroDeclaracion);

    // ✅ AGREGADO: Ideal para usar en el Service antes de guardar y verificar si ya existe
    boolean existsByNumeroDeclaracion(String numeroDeclaracion);
}