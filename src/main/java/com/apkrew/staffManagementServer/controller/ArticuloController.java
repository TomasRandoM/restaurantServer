package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.service.ArticuloServiceImpl;
import com.apkrew.staffManagementServer.domain.service.UnidadDeMedidaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/articulo")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {
    public ArticuloController(ArticuloServiceImpl service) {
        super(service);
    }
}
