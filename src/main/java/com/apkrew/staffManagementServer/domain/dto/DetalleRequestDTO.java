package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.TipoDetalleReciboSueldo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleRequestDTO {
    private Integer cantidad;
    private Double valor;
    private TipoDetalleReciboSueldo tipoDetalleRecibo;
    private String itemReciboDeSueldoId;
}
