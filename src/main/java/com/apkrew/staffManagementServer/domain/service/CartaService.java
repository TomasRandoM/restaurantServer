package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.CartaDTO;
import com.apkrew.staffManagementServer.domain.entity.Carta;

import java.util.List;

public interface CartaService extends BaseService<Carta, String> {
    CartaDTO obtenerCartaActivaDTO() throws Exception;
}
