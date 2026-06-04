package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.SeccionCarta;
import com.apkrew.staffManagementServer.domain.service.SeccionCartaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/seccionCarta")
public class SeccionCartaController
        extends BaseControllerImpl<SeccionCarta, SeccionCartaServiceImpl> {

    public SeccionCartaController(SeccionCartaServiceImpl service) {
        super(service);
    }
}
