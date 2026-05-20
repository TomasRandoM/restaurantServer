package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.DetalleFactura;
import com.apkrew.staffManagementServer.domain.service.DetalleFacturaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/detalleFactura")
public class DetalleFacturaController
        extends BaseControllerImpl<DetalleFactura, DetalleFacturaServiceImpl> {

    public DetalleFacturaController(DetalleFacturaServiceImpl service) {
        super(service);
    }
}
