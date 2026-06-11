package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Comanda;
import com.apkrew.staffManagementServer.domain.dto.ComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.service.ComandaServiceImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.obtenerComandasDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @GetMapping("/paged")
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.obtenerComandasDTO(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.obtenerComandaDTO(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Comanda entity) {
        try {
            Comanda saved = service.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(service.convertToResponseDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Comanda entity) {
        try {
            Comanda updated = service.update(id, entity);
            return ResponseEntity.status(HttpStatus.OK).body(service.convertToResponseDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(service.delete(id));
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
            return ResponseEntity.status(HttpStatus.OK).body(service.convertToResponseDTO(service.facturarComanda(id, formaPagoId, promocionId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<?> entregarComanda(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.convertToResponseDTO(service.entregarComanda(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/entrega-fallida")
    public ResponseEntity<?> marcarEntregaFallida(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.convertToResponseDTO(service.marcarEntregaFallida(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularComanda(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.convertToResponseDTO(service.anularComanda(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
