package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.ItemReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.service.ItemReciboDeSueldoServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/itemReciboDeSueldo")
public class ItemReciboDeSueldoController
        extends BaseControllerImpl<ItemReciboDeSueldo, ItemReciboDeSueldoServiceImpl> {

    public ItemReciboDeSueldoController(ItemReciboDeSueldoServiceImpl service) {
        super(service);
    }
}
