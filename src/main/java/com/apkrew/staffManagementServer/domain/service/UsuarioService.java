package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Domicilio;
import com.apkrew.staffManagementServer.domain.entity.Usuario;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;

public interface UsuarioService extends BaseService<Usuario, String> {
    public Usuario changePassword(Usuario entity) throws Exception;
}
