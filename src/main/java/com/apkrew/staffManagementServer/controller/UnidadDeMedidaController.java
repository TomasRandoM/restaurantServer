package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.service.UnidadDeMedidaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/unidadDeMedida")
public class UnidadDeMedidaController extends BaseControllerImpl<UnidadDeMedida, UnidadDeMedidaServiceImpl> {
    public UnidadDeMedidaController(UnidadDeMedidaServiceImpl service) {
        super(service);
    }
}
