package com.aduanas.com.documentosms.controller;

import com.aduanas.com.documentosms.dto.DocumentoRequestAnalistaDTO;
import com.aduanas.com.documentosms.dto.DocumentoRequestUsuarioExDTO;
import com.aduanas.com.documentosms.dto.DocumentoResponseAnalistaDTO;
import com.aduanas.com.documentosms.dto.DocumentoResponseUsuarioExDTO;
import com.aduanas.com.documentosms.service.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor // Inyecta el Service automáticamente al compilar
public class DocumentoController {

    private final DocumentoService documentoService;

    // ==========================================
    // POST: CLIENTE SUBE DOCUMENTO (FLUJO 1)
    // ==========================================
    @PostMapping("/cliente")
    public ResponseEntity<DocumentoResponseUsuarioExDTO> clienteSubeDocumento(
            @Valid @RequestBody DocumentoRequestUsuarioExDTO dto) {

        // Retorna un 201 Created (Corregido el comentario que decía 211)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoService.clienteSubeDocumento(dto));
    }

    // ==========================================
    // GET: BUSCAR POR ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseAnalistaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.buscarPorId(id));
    }

    // ==========================================
    // GET: LISTAR TODOS (Agregado para dar soporte completo al listar() del Service)
    // ==========================================
    @GetMapping
    public ResponseEntity<List<DocumentoResponseAnalistaDTO>> listar() {
        return ResponseEntity.ok(documentoService.listar());
    }

    // ==========================================
    // PUT: ANALISTA REVISA DOCUMENTO
    // ==========================================
    @PutMapping("/analista/{id}")
    public ResponseEntity<DocumentoResponseAnalistaDTO> analistaRevisaDocumento(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoRequestAnalistaDTO dto) {

        // ✅ CORREGIDO: El Service ya devuelve el DTO directo o lanza la excepción si no existe.
        DocumentoResponseAnalistaDTO response = documentoService.analistaRevisaDocumento(id, dto);
        return ResponseEntity.ok(response);
    }
}