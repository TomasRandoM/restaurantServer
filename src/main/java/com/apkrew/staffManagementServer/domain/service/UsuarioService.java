package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Usuario;

public interface UsuarioService extends BaseService<Usuario, String> {
    public Usuario changePassword(Usuario entity) throws Exception;
}
