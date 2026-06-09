package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoRequestDTO;
import com.apkrew.staffManagementServer.domain.service.ReciboDeSueldoServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/reciboDeSueldo")
public class ReciboDeSueldoController {

    private final ReciboDeSueldoServiceImpl service;

    public ReciboDeSueldoController(ReciboDeSueldoServiceImpl service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ReciboDeSueldoRequestDTO dto) {
        try {
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

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ReciboDeSueldoRequestDTO dto) {
        try {
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

    @GetMapping("/paged")
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findAllResponse(pageable));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(service.delete(id));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/mis-recibos")
    public ResponseEntity<?> getMisRecibos(Pageable pageable) {
        try {
            return ResponseEntity.ok(service.findAllForCurrentUser(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
}
