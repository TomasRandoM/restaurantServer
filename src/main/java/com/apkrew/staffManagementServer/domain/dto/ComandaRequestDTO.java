package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ComandaRequestDTO {
    private EstadoComanda estadoComanda;
    private List<DetalleComandaRequestDTO> detalles;
}
