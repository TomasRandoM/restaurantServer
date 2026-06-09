package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.ComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.service.ComandaServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/comanda")
public class ComandaController {

    private final ComandaServiceImpl service;

    public ComandaController(ComandaServiceImpl service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ComandaRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.saveFromDTO(dto));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error interno. Reintente más tarde.\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ComandaRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateFromDTO(id, dto));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error interno. Reintente más tarde.\"}");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAllResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error al buscar resultados.\"}");
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAllResponse(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error al buscar resultados pautados.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findResponseById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Comanda no encontrada.\"}");
        }
    }

    @PostMapping("/{id}/detalle")
    public ResponseEntity<?> agregarDetalle(@PathVariable String id, @RequestBody DetalleComandaRequestDTO detalleDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.agregarDetalleFromDTO(id, detalleDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/facturar")
    public ResponseEntity<?> facturarComanda(
            @PathVariable String id,
            @RequestParam String formaPagoId,
            @RequestParam(required = false) String promocionId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.facturarComanda(id, formaPagoId, promocionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<?> entregarComanda(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.entregarComanda(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/entrega-fallida")
    public ResponseEntity<?> marcarEntregaFallida(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.marcarEntregaFallida(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularComanda(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.anularComanda(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
