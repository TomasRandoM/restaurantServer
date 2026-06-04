package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CartaListadoDTO {
    private String id;

    private String nombre;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;
}
