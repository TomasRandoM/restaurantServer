package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.FormaDePago;
import com.apkrew.staffManagementServer.domain.service.FormaDePagoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/formaDePago")
public class FormaDePagoController extends BaseControllerImpl<FormaDePago, FormaDePagoServiceImpl>{

    public FormaDePagoController(FormaDePagoServiceImpl service) {
        super(service);
    }
}
