package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Provincia;
import com.apkrew.staffManagementServer.domain.service.ProvinciaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/provincia")
public class ProvinciaController extends BaseControllerImpl<Provincia, ProvinciaServiceImpl>{
    public ProvinciaController(ProvinciaServiceImpl service) {
        super(service);
    }
}
