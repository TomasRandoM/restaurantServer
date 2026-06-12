package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetalleFacturaRequestDTO {
    private int cantidad;
    private double subtotal;
    private String facturaId;
    private List<String> detallesComandaIds;
}
