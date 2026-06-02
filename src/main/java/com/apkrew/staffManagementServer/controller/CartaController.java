package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Carta;
import com.apkrew.staffManagementServer.domain.service.CartaServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/carta")
public class CartaController
        extends BaseControllerImpl<Carta, CartaServiceImpl> {

    public CartaController(CartaServiceImpl service) {
        super(service);
    }

    @GetMapping("/activa/menu")
    public ResponseEntity<?> obtenerCartaActiva() {
        try {
            return ResponseEntity.ok(service.obtenerCartaActivaDTO());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error al obtener la carta\"}");
        }
    }
}
