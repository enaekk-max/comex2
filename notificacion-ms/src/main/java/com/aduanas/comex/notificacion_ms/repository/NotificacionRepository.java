package com.aduanas.comex.notificacion_ms.repository;

import com.aduanas.comex.notificacion_ms.entity.Notificacion;
import com.aduanas.comex.notificacion_ms.enums.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Mantiene tu búsqueda por el Enum de estados
    List<Notificacion> findByEstado(EstadoNotificacion estado);

    // ✅ AGREGADO: Permite buscar el histórico de notificaciones usando el idCarga unificado
    List<Notificacion> findByIdCarga(Long idCarga);
}