package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

@Data
public class ArticuloRequestDTO {
    private String nombre;
    private String descripcion;
    private boolean sinTAC;
    private boolean esIngrediente;
    private String unidadDeMedida;
}
