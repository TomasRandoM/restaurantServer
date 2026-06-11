package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Comanda;
import com.apkrew.staffManagementServer.domain.service.ComandaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/comanda")
public class ComandaController extends BaseControllerImpl<Comanda, ComandaServiceImpl> {

    public ComandaController(ComandaServiceImpl service) {
        super(service);
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
