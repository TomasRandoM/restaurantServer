package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Factura;
import com.apkrew.staffManagementServer.domain.service.FacturaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/factura")
public class FacturaController
        extends BaseControllerImpl<Factura, FacturaServiceImpl> {

    public FacturaController(FacturaServiceImpl service) {
        super(service);
    }
}
