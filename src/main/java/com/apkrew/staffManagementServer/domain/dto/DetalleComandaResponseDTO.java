package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleComandaResponseDTO {
    private String id;
    private Integer cantidad;
    private EstadoDetalleComanda estadoDetalleComanda;
    private Double subtotal;
    private String detalleSeccionCartaId;
    private String articuloNombre;
}
