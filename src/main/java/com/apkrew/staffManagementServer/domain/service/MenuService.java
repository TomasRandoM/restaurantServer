package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.MenuDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuListadoDTO;
import com.apkrew.staffManagementServer.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuService
        extends BaseService<Menu, String> {

    MenuDTO obtenerMenuDTO(String id) throws Exception;

    Page<MenuListadoDTO> obtenerListado(Pageable pageable) throws Exception;

    Menu crearMenu(MenuDTO dto) throws Exception;

    Menu editarMenu(String id, MenuDTO dto) throws Exception;
}
