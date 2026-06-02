package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.service.QRSecretsService;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/qr")
public class QRSecretsController {
    @Autowired
    QRSecretsService qrSecretsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getKey(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(qrSecretsService.getKey(id));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\""+ex.getMessage()+"\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
