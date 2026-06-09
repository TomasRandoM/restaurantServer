package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

@Data
public class MenuDetalleDTO {

    private String id;

    private int cantidad;

    private String articuloId;

    private String articuloNombre;

    private String articuloDescripcion;
}
