package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.LecturaOfflineDTO;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.enums.TipoJustificacion;
import com.apkrew.staffManagementServer.domain.service.RegistroHorarioServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/registroHorario")
public class RegistroHorarioController extends BaseControllerImpl<RegistroHorario, RegistroHorarioServiceImpl>{
    public RegistroHorarioController(RegistroHorarioServiceImpl service) {
        super(service);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> sincronizarLecturasOffline(@RequestBody List<LecturaOfflineDTO> lecturas) {
        try {
            service.sincronizarLecturasOffline(lecturas);
            return ResponseEntity.ok("OK");
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PostMapping(value = "/marcar")
    public ResponseEntity<?> crearJustificacion(
            @RequestParam("empleadoId") String empleadoId) {
        try {
            service.checkRegister(empleadoId);
            return ResponseEntity.ok("OK");
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
