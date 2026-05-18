package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.service.DocumentacionServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/documentacion")
public class DocumentacionController extends BaseControllerImpl<Documentacion, DocumentacionServiceImpl>{
    public DocumentacionController(DocumentacionServiceImpl service) {
        super(service);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<?> getArchivo(@PathVariable String id) {
        try {
            byte[] archivo = service.buscarDocumentacion(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "\"")
                    .body(archivo);
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}