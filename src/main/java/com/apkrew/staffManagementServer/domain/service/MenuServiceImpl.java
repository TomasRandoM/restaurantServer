package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.MenuDetalleDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuListadoDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.TipoImagen;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.MenuRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl
        extends BaseServiceImpl<Menu, String>
        implements MenuService {

    private final MenuRepository menuRepository;
    private final ArticuloService articuloService;
    private final ImageServiceImpl imageService;

    public MenuServiceImpl(
            BaseRepository<Menu, String> baserepository,
            MenuRepository menuRepository,
            ArticuloService articuloService,
            ImageServiceImpl imageService) {

        super(baserepository);
        this.menuRepository = menuRepository;
        this.articuloService = articuloService;
        this.imageService = imageService;
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
        if (m.getImagen() != null) {
            dto.setImagenId(m.getImagen().getId());
        }
        return dto;
    }

    @Override
    @Transactional
    public Menu crearMenu(MenuRequestDTO request, MultipartFile imagen) throws Exception {
        Menu menu = new Menu();
        menu.setNombre(request.getNombre());
        menu.setDescripcion(request.getDescripcion());
        menu.setPrecio(request.getPrecio());
        menu.setDetalles(construirDetalles(menu, request.getDetalles()));

        if (imagen != null && !imagen.isEmpty()) {
            menu.setImagen(guardarImagen(imagen));
        }

        return save(menu);
    }

    @Override
    @Transactional
    public Menu editarMenu(String id, MenuRequestDTO request, MultipartFile imagen) throws Exception {

        Menu menu = findById(id);
        menu.setNombre(request.getNombre());
        menu.setDescripcion(request.getDescripcion());
        menu.setPrecio(request.getPrecio());

        validar(menu, "UPDATE");

        List<DetalleMenu> nuevosDetalles =
                construirDetalles(menu, request.getDetalles());

        if (menu.getDetalles() == null) {
            menu.setDetalles(new ArrayList<>());
        } else {
            menu.getDetalles().clear();
        }
        menu.getDetalles().addAll(nuevosDetalles);

        if (imagen != null && !imagen.isEmpty()) {
            menu.setImagen(guardarImagen(imagen));
        }

        return menuRepository.save(menu);
    }

    private Imagen guardarImagen(MultipartFile file) throws IOException, Exception {
        Imagen img = new Imagen();
        img.setNombre(file.getOriginalFilename());
        img.setMime(file.getContentType());
        img.setContenido(file.getBytes());
        img.setTipoImagen(TipoImagen.PRODUCTO);
        return imageService.save(img);
    }

    @Override
    @Transactional
    public boolean delete(String id) throws Exception {

        Menu menu = findById(id);

        if (menu.isEliminado()) {
            throw new ErrorServiceException(
                    "El menu ya fue eliminado");
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
        if (menu.getImagen() != null) {
            dto.setImagenId(menu.getImagen().getId());
        }

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
