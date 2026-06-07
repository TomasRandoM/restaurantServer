package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Cliente;
import com.apkrew.staffManagementServer.domain.service.ClienteServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/cliente")
public class ClienteController extends BaseControllerImpl<Cliente, ClienteServiceImpl> {
    public ClienteController(ClienteServiceImpl service) {
        super(service);
    }
}
