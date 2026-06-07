package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComandaResponseDTO {
    private String id;
    private Date fechaSolicitudComanda;
    private Date fechaEntregaComanda;
    private EstadoComanda estadoComanda;
    private Double total;
    private List<DetalleComandaResponseDTO> detalles;
    private String facturaId;
    private Long facturaNumero;
}
