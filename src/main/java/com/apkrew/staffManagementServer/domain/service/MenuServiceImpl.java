package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.MenuDetalleDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuListadoDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.MenuRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl
        extends BaseServiceImpl<Menu, String>
        implements MenuService {

    private final MenuRepository menuRepository;
    private final ArticuloService articuloService;

    public MenuServiceImpl(
            BaseRepository<Menu, String> baserepository,
            MenuRepository menuRepository,
            ArticuloService articuloService) {

        super(baserepository);
        this.menuRepository = menuRepository;
        this.articuloService = articuloService;
    }

    @Override
    public boolean validar(Menu entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar el nombre del menú");
            }

            if (entity.getPrecio() <= 0) {
                throw new ErrorServiceException(
                        "El precio debe ser mayor a 0");
            }

            if (entity.getDetalles() == null
                    || entity.getDetalles().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar al menos un artículo");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public MenuDTO obtenerMenuDTO(String id) throws Exception {

        Menu menu = findById(id);

        return convertToDTO(menu);
    }

    public Page<MenuListadoDTO> obtenerListado(Pageable pageable)
            throws Exception {

        Page<Menu> menus =
                menuRepository.findByEliminadoFalse(pageable);

        return menus.map(m -> toListadoDTO(m));
    }

    @Override
    public List<MenuListadoDTO> obtenerListado() throws Exception {

        List<Menu> menus = menuRepository.findByEliminadoFalse();

        List<MenuListadoDTO> result = new ArrayList<>();
        for (Menu m : menus) {
            result.add(toListadoDTO(m));
        }
        return result;
    }

    private MenuListadoDTO toListadoDTO(Menu m) {

        MenuListadoDTO dto = new MenuListadoDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setDescripcion(m.getDescripcion());
        dto.setPrecio(m.getPrecio());
        return dto;
    }

    @Override
    @Transactional
    public Menu crearMenu(MenuDTO dto) throws Exception {

        Menu menu = new Menu();
        cargarDatosMenu(menu, dto);
        menu.setDetalles(construirDetalles(menu, dto.getDetalles()));
        return save(menu);
    }

    @Override
    @Transactional
    public Menu editarMenu(String id, MenuDTO dto) throws Exception {

        Menu menu = findById(id);
        cargarDatosMenu(menu, dto);

        validar(menu, "UPDATE");

        List<DetalleMenu> nuevosDetalles =
                construirDetalles(menu, dto.getDetalles());

        if (menu.getDetalles() == null) {
            menu.setDetalles(new ArrayList<>());
        } else {
            menu.getDetalles().clear();
        }
        menu.getDetalles().addAll(nuevosDetalles);

        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public boolean delete(String id) throws Exception {

        Menu menu = findById(id);

        if (menu.isEliminado()) {
            throw new ErrorServiceException(
                    "El menú ya fue eliminado");
        }

        menu.setEliminado(true);

        if (menu.getDetalles() != null) {
            for (DetalleMenu detalle : menu.getDetalles()) {
                detalle.setEliminado(true);
            }
        }

        menuRepository.save(menu);

        return true;
    }

    private void cargarDatosMenu(Menu menu, MenuDTO dto)
            throws ErrorServiceException {

        menu.setNombre(dto.getNombre());
        menu.setDescripcion(dto.getDescripcion());
        menu.setPrecio(dto.getPrecio());
    }

    private List<DetalleMenu> construirDetalles(
            Menu menu, List<MenuDetalleDTO> detalleDTOs)
            throws Exception {

        List<DetalleMenu> detalles = new ArrayList<>();

        if (detalleDTOs == null) {
            return detalles;
        }

        for (MenuDetalleDTO detalleDTO : detalleDTOs) {

            if (detalleDTO.getArticuloId() == null
                    || detalleDTO.getArticuloId().isEmpty()) {
                continue;
            }

            Articulo articulo =
                    articuloService.findById(detalleDTO.getArticuloId());

            DetalleMenu detalle = new DetalleMenu();
            detalle.setMenu(menu);
            detalle.setArticulo(articulo);
            detalle.setCantidad(detalleDTO.getCantidad());

            detalles.add(detalle);
        }

        return detalles;
    }

    private MenuDTO convertToDTO(Menu menu) {

        MenuDTO dto = new MenuDTO();

        dto.setId(menu.getId());
        dto.setNombre(menu.getNombre());
        dto.setDescripcion(menu.getDescripcion());
        dto.setPrecio(menu.getPrecio());

        List<MenuDetalleDTO> detalles = new ArrayList<>();

        if (menu.getDetalles() != null) {
            for (DetalleMenu detalle : menu.getDetalles()) {

                if (detalle.isEliminado()) {
                    continue;
                }

                MenuDetalleDTO detalleDTO = new MenuDetalleDTO();
                detalleDTO.setId(detalle.getId());
                detalleDTO.setCantidad(detalle.getCantidad());

                if (detalle.getArticulo() != null) {
                    detalleDTO.setArticuloId(
                            detalle.getArticulo().getId());
                    detalleDTO.setArticuloNombre(
                            detalle.getArticulo().getNombre());
                    detalleDTO.setArticuloDescripcion(
                            detalle.getArticulo().getDescripcion());
                }

                detalles.add(detalleDTO);
            }
        }

        dto.setDetalles(detalles);

        return dto;
    }
}
