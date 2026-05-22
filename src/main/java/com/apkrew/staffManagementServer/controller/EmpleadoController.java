package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.EmpleadoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Empleado;
import com.apkrew.staffManagementServer.domain.service.EmpleadoServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/empleado")
public class EmpleadoController extends BaseControllerImpl<Empleado, EmpleadoServiceImpl> {

    public EmpleadoController(EmpleadoServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearEmpleado(
            @ModelAttribute EmpleadoRequestDTO dto,
            @RequestPart("foto") MultipartFile foto) {
        try {
            System.out.println("localidadId recibido: " + dto.toString());
            return ResponseEntity.status(HttpStatus.OK).body(service.crearEmpleado(dto, foto));
        } catch (ErrorServiceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
