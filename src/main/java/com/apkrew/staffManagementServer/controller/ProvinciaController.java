package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Provincia;
import com.apkrew.staffManagementServer.domain.service.ProvinciaServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/provincia")
public class ProvinciaController extends BaseControllerImpl<Provincia, ProvinciaServiceImpl>{
    public ProvinciaController(ProvinciaServiceImpl service) {
        super(service);
    }

    @GetMapping("/pais/{paisId}")
    public ResponseEntity<?> getByPais(@PathVariable String paisId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.findByPais(paisId));

        } catch (ErrorServiceException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + ex.getMessage() + "\"}");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
