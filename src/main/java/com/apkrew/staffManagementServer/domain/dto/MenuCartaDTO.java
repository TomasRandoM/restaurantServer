package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

@Data
public class MenuCartaDTO {

    private String id;

    private String nombre;

    private String descripcion;

    private double precio;

    private String imagenId;

    private String detalleSeccionCartaId;
}
