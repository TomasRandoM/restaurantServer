package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Promocion;
import com.apkrew.staffManagementServer.domain.service.PromocionServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/promocion")
public class PromocionController
        extends BaseControllerImpl<Promocion, PromocionServiceImpl> {

    public PromocionController(PromocionServiceImpl service) {
        super(service);
    }
}
