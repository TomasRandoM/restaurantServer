package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.DetalleMenu;
import com.apkrew.staffManagementServer.domain.service.DetalleMenuServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/detalleMenu")
public class DetalleMenuController
        extends BaseControllerImpl<DetalleMenu, DetalleMenuServiceImpl> {

    public DetalleMenuController(DetalleMenuServiceImpl service) {
        super(service);
    }
}
