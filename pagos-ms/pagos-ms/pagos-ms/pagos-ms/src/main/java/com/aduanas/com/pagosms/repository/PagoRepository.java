package com.aduanas.com.pagosms.repository;

import com.aduanas.com.pagosms.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // ✅ AGREGADO: Para que Spring lo registre en el contenedor de dependencias
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // No necesita nada más. Todos los métodos CRUD financieros ya están disponibles.
}