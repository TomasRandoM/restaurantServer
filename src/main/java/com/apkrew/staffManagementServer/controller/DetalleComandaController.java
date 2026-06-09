package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.apkrew.staffManagementServer.domain.service.DetalleComandaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/detalle-comanda")
public class DetalleComandaController {

    private final DetalleComandaServiceImpl service;

    public DetalleComandaController(DetalleComandaServiceImpl service) {
        this.service = service;
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable String id, @RequestParam EstadoDetalleComanda nuevoEstado) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.cambiarEstado(id, nuevoEstado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
