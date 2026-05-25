package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Stock;
import com.apkrew.staffManagementServer.domain.service.StockServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/stock")
public class StockController extends BaseControllerImpl<Stock, StockServiceImpl> {
    public StockController(StockServiceImpl stockService) {
        super(stockService);
    }
}
