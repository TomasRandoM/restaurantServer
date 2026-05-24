package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.QRSecrets;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;

public interface QRSecretsService {
    public boolean validar(QRSecrets entity, String caso) throws Exception;
    public String getKey(String empleadoDNI) throws ErrorServiceException;
    public void initializeKey();
}
