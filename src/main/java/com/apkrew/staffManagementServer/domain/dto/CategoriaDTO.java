package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaDTO {

    private String id;

    private String nombre;

    private List<ArticuloCartaDTO> productos;
}
