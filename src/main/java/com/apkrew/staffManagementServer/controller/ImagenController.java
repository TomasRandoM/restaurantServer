package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Imagen;
import com.apkrew.staffManagementServer.domain.service.ImageServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/imagen")
public class ImagenController {

    private final ImageServiceImpl imageService;

    public ImagenController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImagen(@PathVariable String id) {
        try {
            Imagen imagen = imageService.findById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(imagen.getMime()));
            headers.setContentLength(imagen.getContenido().length);
            headers.setCacheControl("max-age=86400, public");
            return new ResponseEntity<>(imagen.getContenido(), headers, HttpStatus.OK);
        } catch (ErrorServiceException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
