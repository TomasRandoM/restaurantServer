package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.service.ReciboDeSueldoServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/reciboDeSueldo")
public class ReciboDeSueldoController
        extends BaseControllerImpl<ReciboDeSueldo, ReciboDeSueldoServiceImpl> {

    public ReciboDeSueldoController(ReciboDeSueldoServiceImpl service) {
        super(service);
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
