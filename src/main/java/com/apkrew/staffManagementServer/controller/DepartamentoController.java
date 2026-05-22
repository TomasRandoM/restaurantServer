package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Departamento;
import com.apkrew.staffManagementServer.domain.service.DepartamentoServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/departamento")
public class DepartamentoController
        extends BaseControllerImpl<Departamento, DepartamentoServiceImpl> {

    public DepartamentoController(DepartamentoServiceImpl service) {
        super(service);
    }

    @GetMapping("/provincia/{provinciaId}")
    public ResponseEntity<?> getByProvincia(@PathVariable String provinciaId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findByProvincia(provinciaId));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
