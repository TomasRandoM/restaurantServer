package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.MenuRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Menu;
import com.apkrew.staffManagementServer.domain.service.MenuServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/menu")
public class MenuController
        extends BaseControllerImpl<Menu, MenuServiceImpl> {

    public MenuController(MenuServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearMenu(
            @ModelAttribute MenuRequestDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.crearMenu(dto, imagen));
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.obtenerMenuDTO(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/listado")
    public ResponseEntity<?> listado(Pageable pageable) {
        try {
            return ResponseEntity.ok(service.obtenerListado(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/listado-simple")
    public ResponseEntity<?> listadoSimple() {
        try {
            return ResponseEntity.ok(service.obtenerListado());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping(value = "/editar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editarMenu(
            @PathVariable String id,
            @ModelAttribute MenuRequestDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Menu menu = service.editarMenu(id, dto, imagen);
            return ResponseEntity.status(HttpStatus.OK).body(menu);
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
