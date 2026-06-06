package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.MenuDTO;
import com.apkrew.staffManagementServer.domain.entity.Menu;
import com.apkrew.staffManagementServer.domain.service.MenuServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/menu")
public class MenuController
        extends BaseControllerImpl<Menu, MenuServiceImpl> {

    public MenuController(MenuServiceImpl service) {
        super(service);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearMenu(@RequestBody MenuDTO dto) {
        try {
            return ResponseEntity.ok(service.crearMenu(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.obtenerMenuDTO(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<?> listado(Pageable pageable) {
        try {
            return ResponseEntity.ok(service.obtenerListado(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/listado-simple")
    public ResponseEntity<?> listadoSimple() {
        try {
            return ResponseEntity.ok(service.obtenerListado());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarMenu(
            @PathVariable String id,
            @RequestBody MenuDTO dto) {
        try {
            return ResponseEntity.ok(service.editarMenu(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
