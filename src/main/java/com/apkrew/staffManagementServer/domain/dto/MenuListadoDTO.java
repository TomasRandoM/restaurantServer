package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

@Data
public class MenuListadoDTO {

    private String id;

    private String nombre;

    private String descripcion;

    private double precio;

    private String imagenId;
}
