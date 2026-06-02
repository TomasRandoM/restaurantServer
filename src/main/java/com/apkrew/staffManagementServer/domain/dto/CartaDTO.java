package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CartaDTO {

    private String id;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    private List<CategoriaDTO> categorias;
}
