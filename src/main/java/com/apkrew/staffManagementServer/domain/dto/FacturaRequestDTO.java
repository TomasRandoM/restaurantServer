package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FacturaRequestDTO {
    private Long numeroFactura;
    private Date fechaFactura;
    private double totalPagado;
    private EstadoFactura estado;
    private String formaPagoId;
    private String promocionId;
    private List<String> comandaIds;
}
