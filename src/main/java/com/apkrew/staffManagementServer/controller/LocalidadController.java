package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Localidad;
import com.apkrew.staffManagementServer.domain.service.LocalidadServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/localidad")
public class LocalidadController extends BaseControllerImpl<Localidad, LocalidadServiceImpl>{
    public LocalidadController(LocalidadServiceImpl service) {
        super(service);
    }

    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<?> getByDepartamento(@PathVariable String departamentoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findByDepartamento(departamentoId));

        } catch (ErrorServiceException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }


}
