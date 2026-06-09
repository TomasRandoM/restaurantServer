package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.MenuDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuListadoDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService
        extends BaseService<Menu, String> {

    MenuDTO obtenerMenuDTO(String id) throws Exception;

    Page<MenuListadoDTO> obtenerListado(Pageable pageable) throws Exception;

    List<MenuListadoDTO> obtenerListado() throws Exception;

    Menu crearMenu(MenuRequestDTO dto, MultipartFile imagen) throws Exception;

    Menu editarMenu(String id, MenuRequestDTO dto, MultipartFile imagen) throws Exception;
}
