package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.MovimientoStock;
import com.apkrew.staffManagementServer.domain.service.MovimientoStockServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/movimiento-stock")
public class MovimientoStockController extends BaseControllerImpl<MovimientoStock, MovimientoStockServiceImpl> {
    public MovimientoStockController(MovimientoStockServiceImpl service) {
        super(service);
    }
}
