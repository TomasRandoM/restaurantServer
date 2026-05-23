package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoJustificacion;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentacionService extends BaseService<Documentacion, String> {
    public Documentacion crearDocumentacion(TipoJustificacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException;
    public byte[] buscarDocumentacion(String id) throws ErrorServiceException;
    public Documentacion modificarDocumentacion(Documentacion documentacion, MultipartFile archivo) throws ErrorServiceException;
}
