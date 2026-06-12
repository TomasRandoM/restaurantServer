package com.apkrew.staffManagementServer.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DetalleFacturaResponseDTO {
    private String id;
    private int cantidad;
    private double subtotal;
    private String facturaId;
    private String comandaId;
    private boolean eliminado;
    private List<DetalleComandaResponseDTO> detallesComanda;
}
