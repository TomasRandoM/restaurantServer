package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.DetalleReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.service.DetalleReciboDeSueldoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/detalleReciboDeSueldo")
public class DetalleReciboDeSueldoController
        extends BaseControllerImpl<DetalleReciboDeSueldo, DetalleReciboDeSueldoServiceImpl> {

    public DetalleReciboDeSueldoController(DetalleReciboDeSueldoServiceImpl service) {
        super(service);
    }
}
