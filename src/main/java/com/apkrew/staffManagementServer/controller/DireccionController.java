package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Direccion;
import com.apkrew.staffManagementServer.domain.service.DireccionServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/direccion")
public class DireccionController extends BaseControllerImpl<Direccion, DireccionServiceImpl> {
    public DireccionController(DireccionServiceImpl service) {
        super(service);
    }
}
