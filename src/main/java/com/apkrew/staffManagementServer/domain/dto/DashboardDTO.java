package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardDTO {

    private int clientes;
    private int empleados;
    private int stockCritico;
}
