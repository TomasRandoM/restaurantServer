package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {

    private String id;

    private String nombre;

    private String descripcion;

    private double precio;

    private List<MenuDetalleDTO> detalles;

    private String imagenId;
}
