package com.apkrew.staffManagementServer.domain.dto;

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
public class ReciboDeSueldoResponseDTO {
    private String id;
    private String empleadoId;
    private String empleadoNombre;
    private String empleadoApellido;
    private Date fechaDePago;
    private Integer mesPago;
    private Double totalPago;
    private String observacion;
    private List<DetalleResponseDTO> detalles;
}
