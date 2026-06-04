package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.CartaDTO;
import com.apkrew.staffManagementServer.domain.entity.Carta;
import com.apkrew.staffManagementServer.domain.service.CartaServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/carta")
public class CartaController
        extends BaseControllerImpl<Carta, CartaServiceImpl> {

    public CartaController(CartaServiceImpl service) {
        super(service);
    }

    @GetMapping("/activa")
    public ResponseEntity<?> obtenerCartaActiva() {
        try {
            return ResponseEntity.ok(service.obtenerCartaActivaDTO());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error al obtener la carta\"}");
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCarta(
            @RequestBody CartaDTO dto) {
        try {
            return ResponseEntity.ok(
                    service.crearCarta(dto)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable String id) {

        try {

            return ResponseEntity.ok(
                    service.obtenerCartaDTO(id)
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<?> listado(
            Pageable pageable) {

        try {

            return ResponseEntity.ok(
                    service.obtenerListado(pageable)
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarCarta(
            @PathVariable String id,
            @RequestBody CartaDTO dto) throws Exception{

        try {

            return ResponseEntity.ok(
                    service.editarCarta(id, dto)
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
