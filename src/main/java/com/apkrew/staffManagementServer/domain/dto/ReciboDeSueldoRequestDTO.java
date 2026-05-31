package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ReciboDeSueldoRequestDTO {
    private String empleadoId;
    private Date fechaDePago;
    private Integer mesPago;
    private String observacion;
    private List<DetalleRequestDTO> detalles;
}
