package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {

    private String id;

    private String nombre;

    private double precio;

    private List<MenuDetalleDTO> detalles;
}
