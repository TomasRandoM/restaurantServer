package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class FacturaResponseDTO {
    private String id;
    private Long numeroFactura;
    private Date fechaFactura;
    private double totalPagado;
    private EstadoFactura estado;
    private String formaPagoId;
    private String promocionId;
    private boolean eliminado;
    private List<DetalleFacturaResponseDTO> detalles;
}
