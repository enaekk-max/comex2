package com.aduanas.com.pagosms.controller;

import com.aduanas.com.pagosms.dto.PagoRequestDto;
import com.aduanas.com.pagosms.dto.PagoResponseDto;
import com.aduanas.com.pagosms.dto.NotificacionBancoDto;
import com.aduanas.com.pagosms.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    // ==========================================
    // POST: REGISTRAR INTENTO DE PAGO (PENDIENTE)
    // ==========================================
    @PostMapping
    public ResponseEntity<PagoResponseDto> registrarPago(@Valid @RequestBody PagoRequestDto requestDto) {
        PagoResponseDto respuesta = pagoService.procesarPago(requestDto);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED); // Retorna 201 Created
    }

    // ==========================================
    // POST: WEBHOOK SIMULADOR DEL BANCO (COMPLETADO / FALLIDO)
    // ==========================================
    @PostMapping("/notificacion-banco")
    public ResponseEntity<PagoResponseDto> recibirNotificacionBanco(@Valid @RequestBody NotificacionBancoDto bancoDto) {
        // Modifica el estado del pago y gatilla la liberación remota en carga-ms
        PagoResponseDto respuesta = pagoService.confirmarPagoDesdeBanco(bancoDto);
        return ResponseEntity.ok(respuesta); // Retorna 200 OK
    }

    // ==========================================
    // GET: LISTAR TODOS LOS PAGOS
    // ==========================================
    @GetMapping
    public ResponseEntity<List<PagoResponseDto>> listarTodos() {
        List<PagoResponseDto> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }

    // ==========================================
    // GET: BUSCAR PAGO POR ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDto> buscarPorId(@PathVariable Long id) {
        PagoResponseDto pago = pagoService.obtenerPagoPorId(id);
        return ResponseEntity.ok(pago);
    }

    // Este endpoint recibirá la llamada de Clasificación
    @PostMapping("/crear-orden")
    public ResponseEntity<Void> crearOrdenDesdeClasificacion(
            @RequestParam("idCarga") Long idCarga,
            @RequestParam("monto") java.math.BigDecimal monto) {

        // Creamos el objeto para tu lógica de negocio
        PagoRequestDto dto = PagoRequestDto.builder()
                .idCarga(idCarga)
                .monto(monto)
                .moneda("CLP")
                .idTransaccionExterna("PENDIENTE-" + idCarga)
                .build();

        pagoService.procesarPago(dto); // Esto guarda en la DB
        return ResponseEntity.ok().build();
    }
}