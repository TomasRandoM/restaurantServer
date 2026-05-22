package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Localidad;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;

import java.util.List;

public interface LocalidadService extends BaseService<Localidad, String>{
    public List<Localidad> findByDepartamento(String departamentoId) throws ErrorServiceException;
}
