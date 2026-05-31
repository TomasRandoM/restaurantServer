package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.DetalleRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.DetalleReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.service.ReciboDeSueldoServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/reciboDeSueldo")
public class ReciboDeSueldoController
        extends BaseControllerImpl<ReciboDeSueldo, ReciboDeSueldoServiceImpl> {

    public ReciboDeSueldoController(ReciboDeSueldoServiceImpl service) {
        super(service);
    }

    @Override
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ReciboDeSueldo entity) {
        try {
            ReciboDeSueldoRequestDTO dto = toRequestDTO(entity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.saveFromDTO(dto));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ReciboDeSueldo entity) {
        try {
            ReciboDeSueldoRequestDTO dto = toRequestDTO(entity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.updateFromDTO(id, dto));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findAllResponse());
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findResponseById(id));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/calcularTotal/{id}")
    public ResponseEntity<?> calcularTotal(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.calcularTotal(id));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    private ReciboDeSueldoRequestDTO toRequestDTO(ReciboDeSueldo entity) {
        ReciboDeSueldoRequestDTO dto = new ReciboDeSueldoRequestDTO();

        dto.setEmpleadoId(entity.getEmpleado() != null
                ? entity.getEmpleado().getId() : null);
        dto.setFechaDePago(entity.getFechaDePago());
        dto.setMesPago(entity.getMesPago());
        dto.setObservacion(entity.getObservacion());

        if (entity.getDetalles() != null) {
            dto.setDetalles(new ArrayList<>());
            for (DetalleReciboDeSueldo det : entity.getDetalles()) {
                DetalleRequestDTO detDTO = new DetalleRequestDTO();
                detDTO.setCantidad(det.getCantidad());
                detDTO.setValor(det.getValor());
                detDTO.setTipoDetalleRecibo(det.getTipoDetalleRecibo());
                detDTO.setItemReciboDeSueldoId(
                        det.getItemReciboDeSueldo() != null
                                ? det.getItemReciboDeSueldo().getId() : null);
                dto.getDetalles().add(detDTO);
            }
        }

        return dto;
    }
}
