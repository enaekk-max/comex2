package com.aduanas.com.pagosms.service;

import com.aduanas.com.pagosms.Enum.EstadoCarga;
import com.aduanas.com.pagosms.Enum.EstadoPago;
import com.aduanas.com.pagosms.client.ClasificacionFeignClient;
import com.aduanas.com.pagosms.dto.CargaExternaDto;
import com.aduanas.com.pagosms.dto.PagoRequestDto;
import com.aduanas.com.pagosms.dto.PagoResponseDto;
import com.aduanas.com.pagosms.dto.NotificacionBancoDto;
import com.aduanas.com.pagosms.entity.Pago;
import com.aduanas.com.pagosms.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ClasificacionFeignClient clasificacionFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public PagoResponseDto procesarPago(PagoRequestDto dto) {
        // ✅ CORREGIDO: idCarga alineado con tu DTO
        CargaExternaDto carga = clasificacionFeignClient.obtenerCargaPorId(dto.getIdCarga());

        if (carga == null) {
            throw new RuntimeException("La carga con ID " + dto.getIdCarga() + " no existe en el sistema.");
        }

        // ✅ TU LÓGICA DE VALIDACIÓN ORIGINAL
        if (!EstadoCarga.PENDIENTE_PAGO.name().equals(carga.getEstado())) {
            throw new RuntimeException("No se puede procesar el pago. La carga requiere estar PENDIENTE_PAGO. Estado actual: " + carga.getEstado());
        }

        BigDecimal peso = carga.getPeso();
        BigDecimal montoBase;
        String cargoFijo = "$0 CLP";
        String tramo;

        BigDecimal mil = new BigDecimal("1000");
        BigDecimal dosMil = new BigDecimal("2000");
        BigDecimal tresMil = new BigDecimal("3000");
        BigDecimal cuatroMil = new BigDecimal("4000");
        BigDecimal cincoMil = new BigDecimal("5000");

        // ✅ TRAMOS ORIGINALES
        if (peso.compareTo(mil) >= 0 && peso.compareTo(new BigDecimal("1999")) <= 0) {
            montoBase = new BigDecimal("200000").add(new BigDecimal("50000"));
            tramo = "Tramo 1: 1.000kg a 1.999kg (Base: 200.000 CLP)";
            cargoFijo = "$50.000 CLP";
        } else if (peso.compareTo(dosMil) >= 0 && peso.compareTo(new BigDecimal("2999")) <= 0) {
            montoBase = new BigDecimal("160000");
            tramo = "Tramo 2: 2.000kg a 2.999kg (Base: 160.000 CLP)";
        } else if (peso.compareTo(tresMil) >= 0 && peso.compareTo(new BigDecimal("3999")) <= 0) {
            montoBase = new BigDecimal("110000");
            tramo = "Tramo 3: 3.000kg a 3.999kg (Base: 110.000 CLP)";
        } else if (peso.compareTo(cuatroMil) >= 0 && peso.compareTo(new BigDecimal("4999")) <= 0) {
            montoBase = new BigDecimal("90000");
            tramo = "Tramo 4: 4.000kg a 4.999kg (Base: 90.000 CLP)";
        } else if (peso.compareTo(cincoMil) >= 0) {
            montoBase = new BigDecimal("70000");
            tramo = "Tramo Alto: Mayor a 5.000kg (Base: 70.000 CLP)";
        } else {
            montoBase = new BigDecimal("50000");
            tramo = "Carga General Básica - Menor a 1.000kg";
        }

        BigDecimal valorIva = carga.getMontoImpuesto();
        if (valorIva == null) {
            valorIva = BigDecimal.ZERO;
        }

        BigDecimal montoCalculado = montoBase.add(valorIva).setScale(2, RoundingMode.HALF_UP);

        // ✅ CONSTRUCCIÓN DE ENTIDAD
        Pago nuevoPago = Pago.builder()
                .idCarga(dto.getIdCarga())
                .monto(montoCalculado)
                .moneda(dto.getMoneda())
                .estadoPago(EstadoPago.PENDIENTE)
                .idTransaccionExterna(dto.getIdTransaccionExterna())
                .fechaPago(LocalDateTime.now())
                .build();

        Pago pagoGuardado = pagoRepository.save(nuevoPago);

        return mapToResponseDTO(pagoGuardado, tramo, cargoFijo, valorIva);
    }

    @Transactional(rollbackFor = Exception.class)
    public PagoResponseDto confirmarPagoDesdeBanco(NotificacionBancoDto bancoDto) {
        // ✅ CORREGIDO: idPago
        Pago pago = pagoRepository.findById(bancoDto.getIdPago())
                .orElseThrow(() -> new RuntimeException("El pago con ID " + bancoDto.getIdPago() + " no existe."));

        pago.setEstadoPago(EstadoPago.PROCESANDO);
        pagoRepository.save(pago);

        String tramo = "No disponible";
        String cargoFijo = "$0 CLP";
        BigDecimal impuestoReal = BigDecimal.ZERO;

        try {
            CargaExternaDto c = clasificacionFeignClient.obtenerCargaPorId(bancoDto.getIdCarga());
            if (c != null) {
                impuestoReal = c.getMontoImpuesto() != null ? c.getMontoImpuesto() : BigDecimal.ZERO;
                tramo = "Carga Verificada (" + c.getPeso() + " kg)";
            }
        } catch (Exception e) {
            log.error("No se pudo obtener la carga mediante ClasificacionFeignClient: {}", e.getMessage());
        }

        if (bancoDto.getTransaccionExitosa()) {
            pago.setEstadoPago(EstadoPago.COMPLETADO);
            pago.setIdTransaccionExterna(bancoDto.getIdTransaccionExterna());
            pago.setFechaPago(LocalDateTime.now());

            pagoRepository.save(pago);

            // ✅ TU LOGICA ORIGINAL SIN COSAS RARAS: Llama con el String "LIBERADA"
            try {
                clasificacionFeignClient.actualizarEstadoLiberacion(bancoDto.getIdCarga(), "LIBERADA");
            } catch (Exception e) {
                throw new RuntimeException("Pago completado, pero falló la comunicación remota con Clasificación: " + e.getMessage());
            }
        } else {
            pago.setEstadoPago(EstadoPago.FALLIDO);
            pagoRepository.save(pago);
        }

        return mapToResponseDTO(pago, tramo, cargoFijo, impuestoReal);
    }

    public List<PagoResponseDto> obtenerTodosLosPagos() {
        return pagoRepository.findAll().stream()
                .map(p -> {
                    String tramo = "Carga General";
                    BigDecimal impuestoReal = BigDecimal.ZERO;
                    try {
                        CargaExternaDto c = clasificacionFeignClient.obtenerCargaPorId(p.getIdCarga());
                        if (c != null) {
                            if (c.getMontoImpuesto() != null) impuestoReal = c.getMontoImpuesto();
                            tramo = "Carga de " + c.getPeso() + " kg";
                        }
                    } catch (Exception e) {}
                    return mapToResponseDTO(p, tramo, "$0 CLP", impuestoReal);
                })
                .toList();
    }

    public PagoResponseDto obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pago con ID " + id + " no existe en los registros."));

        String tramo = "Carga General";
        BigDecimal impuestoReal = BigDecimal.ZERO;
        try {
            CargaExternaDto c = clasificacionFeignClient.obtenerCargaPorId(pago.getIdCarga());
            if (c != null) {
                if (c.getMontoImpuesto() != null) impuestoReal = c.getMontoImpuesto();
                tramo = "Carga de " + c.getPeso() + " kg";
            }
        } catch (Exception e) {}

        return mapToResponseDTO(pago, tramo, "$0 CLP", impuestoReal);
    }

    private PagoResponseDto mapToResponseDTO(Pago p, String tramo, String cargoFijo, BigDecimal valorIva) {
        return PagoResponseDto.builder()
                .idPago(p.getIdPago())
                .idCarga(p.getIdCarga())
                .tramo(tramo)
                .cargoFijo(cargoFijo)
                .montoImpuesto(valorIva)
                .montoTotal(p.getMonto())
                .moneda(p.getMoneda())
                .estadoPago(p.getEstadoPago() != null ? p.getEstadoPago().name() : null)
                .idTransaccionExterna(p.getIdTransaccionExterna())
                .fechaPago(p.getFechaPago())
                .build();
    }
}