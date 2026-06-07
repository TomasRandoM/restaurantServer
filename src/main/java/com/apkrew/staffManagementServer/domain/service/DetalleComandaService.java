package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;

public interface DetalleComandaService extends BaseService<DetalleComanda, String> {
    DetalleComanda cambiarEstado(String id, EstadoDetalleComanda nuevoEstado) throws Exception;
}
