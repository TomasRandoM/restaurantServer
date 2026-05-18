package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Justificacion;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface JustificacionService extends BaseService<Justificacion, String> {
    Justificacion crearJustificacion(String registroHorarioId, TipoDocumentacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException;
}
