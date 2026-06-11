package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.HistorialVisitasRestaurant;
import com.apkrew.staffManagementServer.domain.service.HistorialVisitasRestaurantServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/historial-visitas-restaurant")
public class HistorialVisitasRestaurantController extends BaseControllerImpl<HistorialVisitasRestaurant, HistorialVisitasRestaurantServiceImpl> {
    public HistorialVisitasRestaurantController(HistorialVisitasRestaurantServiceImpl service) {
        super(service);
    }
}
