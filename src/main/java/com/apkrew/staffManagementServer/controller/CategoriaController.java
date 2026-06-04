package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Categoria;
import com.apkrew.staffManagementServer.domain.service.CategoriaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/categoria")
public class CategoriaController extends BaseControllerImpl<Categoria, CategoriaServiceImpl> {

    public CategoriaController(CategoriaServiceImpl service) {
        super(service);
    }
}
