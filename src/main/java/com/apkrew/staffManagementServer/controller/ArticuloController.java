package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.ArticuloRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.service.ArticuloServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/articulo")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {
    public ArticuloController(ArticuloServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearArticulo(
            @ModelAttribute ArticuloRequestDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.crearArticulo(dto, imagen));
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PutMapping(value = "/editar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editarArticulo(
            @PathVariable String id,
            @ModelAttribute ArticuloRequestDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Articulo articulo = service.editarArticulo(id, dto, imagen);
            return ResponseEntity.status(HttpStatus.OK).body(articulo);
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
