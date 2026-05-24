package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;

public interface ReciboDeSueldoService extends BaseService<ReciboDeSueldo, String> {

    double calcularTotal(String id) throws Exception;
}
