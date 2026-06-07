package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaDTO {

    private String id;

    private String nombre;

    private int orden;

    private List<ArticuloCartaDTO> productos;

    private List<MenuCartaDTO> menus;
}
