package com.aduana.bancoms.service;

import com.aduana.bancoms.client.PagoFeignClient;
import com.aduana.bancoms.dto.NotificacionBancoDto;
import com.aduana.bancoms.dto.TransaccionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BancoService {

    private final PagoFeignClient pagoFeignClient;

    public String procesarTransaccionBancaria(TransaccionRequestDto dto) {

        // 1. SIMULACIÓN: Si la tarjeta empieza con "4444", el banco la rechaza
        boolean fondosSuficientes = !dto.getNumeroTarjeta().startsWith("4444");

        // 2. GENERADOR DE VÁUCHER: Código de autorización único
        String voucherId = "VOUCHER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 3. CONSTRUCCIÓN DE LA RESPUESTA: Usando los campos unificados 'idPago' e 'idCarga'
        NotificacionBancoDto notificacion = new NotificacionBancoDto();
        notificacion.setIdPago(dto.getIdPago());   // ✅ CORREGIDO: de 'pagoId' a 'idPago'
        notificacion.setIdCarga(dto.getIdCarga()); // ✅ CORREGIDO: de 'cargaId' a 'idCarga'
        notificacion.setIdTransaccionExterna(voucherId);
        notificacion.setTransaccionExitosa(fondosSuficientes);

        // 4. EL DISPARO DE FEIGN: Notifica en vivo a pagos-ms (Puerto 8083)
        try {
            pagoFeignClient.enviarConfirmacionAlMicroservicioPagos(notificacion);
        } catch (Exception e) {
            // Si pagos-ms está caído o explota, el banco frena todo
            throw new RuntimeException("El banco procesó el dinero, pero el sistema de Aduanas (pagos-ms) no responde: " + e.getMessage());
        }

        // 5. RESPUESTA FINAL PARA POSTMAN
        if (!fondosSuficientes) {
            throw new RuntimeException("Transacción RECHAZADA por el banco. Fondos insuficientes en la tarjeta.");
        }

        return "Transacción APROBADA con éxito. Código de autorización: " + voucherId;
    }
}