package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DashboardDTO;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ClienteServiceImpl clienteServiceImpl;
    private final StockServiceImpl stockServiceImpl;
    private final EmpleadoServiceImpl empleadoServiceImpl;

    public DashboardService(ClienteServiceImpl clienteServiceImpl,
                            StockServiceImpl stockServiceImpl,
                            EmpleadoServiceImpl empleadoServiceImpl) {
        this.clienteServiceImpl = clienteServiceImpl;
        this.stockServiceImpl = stockServiceImpl;
        this.empleadoServiceImpl = empleadoServiceImpl;
    }

    public DashboardDTO getDashboard() {
        DashboardDTO dashboardDTO = new DashboardDTO();

        dashboardDTO.setClientes(clienteServiceImpl.contarClientesActivos());
        dashboardDTO.setEmpleados(empleadoServiceImpl.contarEmpleadosActivos());
        dashboardDTO.setStockCritico(stockServiceImpl.countStockCritico());

        return dashboardDTO;
    }

}
