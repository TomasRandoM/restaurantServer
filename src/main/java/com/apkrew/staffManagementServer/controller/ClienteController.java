package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.dto.ClienteRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.EmpleadoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Cliente;
import com.apkrew.staffManagementServer.domain.service.ClienteServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/cliente")
public class ClienteController extends BaseControllerImpl<Cliente, ClienteServiceImpl> {
    public ClienteController(ClienteServiceImpl service) {
        super(service);
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<?> crearCliente(
            @RequestBody ClienteRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.crearCliente(dto));
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
}
