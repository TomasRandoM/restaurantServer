package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComandaResponseDTO {
    private String id;
    private LocalDateTime fechaSolicitudComanda;
    private LocalDateTime fechaEntregaComanda;
    private EstadoComanda estadoComanda;
    private Long facturaNumero;
    private String clienteId;
    private String clienteNombre;
    private double total;
    private List<DetalleComandaResponseDTO> detalles;
}
