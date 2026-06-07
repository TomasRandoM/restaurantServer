package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleComandaRequestDTO {
    private Integer cantidad;
    private EstadoDetalleComanda estadoDetalleComanda;
    private String detalleSeccionCartaId;
}
