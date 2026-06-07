package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.LecturaOfflineDTO;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;

import java.util.List;

public interface RegistroHorarioService extends BaseService<RegistroHorario, String> {
    List<RegistroHorario> sincronizarLecturasOffline(List<LecturaOfflineDTO> lecturas) throws Exception;
}
