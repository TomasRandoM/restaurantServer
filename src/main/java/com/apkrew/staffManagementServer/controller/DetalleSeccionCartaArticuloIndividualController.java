package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.DetalleSeccionCartaArticuloIndividual;
import com.apkrew.staffManagementServer.domain.service.DetalleSeccionCartaArticuloIndividualServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/detalleSeccionCartaArticuloIndividual")
public class DetalleSeccionCartaArticuloIndividualController
        extends BaseControllerImpl<
        DetalleSeccionCartaArticuloIndividual,
        DetalleSeccionCartaArticuloIndividualServiceImpl> {

    public DetalleSeccionCartaArticuloIndividualController(
            DetalleSeccionCartaArticuloIndividualServiceImpl service) {

        super(service);
    }
}
