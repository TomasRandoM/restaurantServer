package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Justificacion;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.service.JustificacionServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/justificacion")
public class JustificacionController extends BaseControllerImpl<Justificacion, JustificacionServiceImpl> {

    public JustificacionController(JustificacionServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearJustificacion(
            @RequestParam("registroHorarioId") String registroHorarioId,
            @RequestParam("tipoDocumentacion") TipoDocumentacion tipoDocumentacion,
            @RequestParam(value = "observacion", required = false) String observacion,
            @RequestPart("archivo") MultipartFile archivo) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.crearJustificacion(registroHorarioId, tipoDocumentacion, observacion, archivo));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
