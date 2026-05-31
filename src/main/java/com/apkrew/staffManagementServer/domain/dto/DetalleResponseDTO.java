package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.TipoDetalleReciboSueldo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleResponseDTO {
    private String id;
    private Integer cantidad;
    private Double valor;
    private TipoDetalleReciboSueldo tipoDetalleRecibo;
    private String itemReciboDeSueldoId;
    private String itemReciboDeSueldoNombre;
}
