package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Pais;
import com.apkrew.staffManagementServer.domain.service.PaisServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/pais")
public class PaisController extends BaseControllerImpl<Pais, PaisServiceImpl>{
    public PaisController(PaisServiceImpl service) {
        super(service);
    }
}
