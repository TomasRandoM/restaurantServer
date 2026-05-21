package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Departamento;
import com.apkrew.staffManagementServer.domain.service.DepartamentoServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/departamento")
public class DepartamentoController
        extends BaseControllerImpl<Departamento, DepartamentoServiceImpl> {

    public DepartamentoController(DepartamentoServiceImpl service) {
        super(service);
    }
}
