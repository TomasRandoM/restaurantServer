package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Pais;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.service.PaisServiceImpl;
import com.apkrew.staffManagementServer.domain.service.RegistroHorarioServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/registroHorario")
public class RegistroHorarioController extends BaseControllerImpl<RegistroHorario, RegistroHorarioServiceImpl>{
    public RegistroHorarioController(RegistroHorarioServiceImpl service) {
        super(service);
    }
}
