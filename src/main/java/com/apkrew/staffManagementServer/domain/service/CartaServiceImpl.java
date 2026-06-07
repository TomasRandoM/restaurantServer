package com.apkrew.staffManagementServer.domain.service;


import com.apkrew.staffManagementServer.domain.dto.ArticuloCartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CartaListadoDTO;
import com.apkrew.staffManagementServer.domain.dto.CategoriaDTO;
import com.apkrew.staffManagementServer.domain.dto.MenuCartaDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.CartaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CartaServiceImpl extends BaseServiceImpl<Carta, String>
        implements CartaService {

    private final CartaRepository cartaRepository;

    private final CategoriaService categoriaService;
    private final ArticuloService articuloService;
    private final MenuService menuService;

    public CartaServiceImpl(
            BaseRepository<Carta, String> baserepository,
            CartaRepository cartaRepository,
            CategoriaService categoriaService,
            ArticuloService articuloService,
            MenuService menuService) {

        super(baserepository);

        this.cartaRepository = cartaRepository;
        this.categoriaService = categoriaService;
        this.articuloService = articuloService;
        this.menuService = menuService;
    }

    @Override
    public boolean validar(Carta entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar el nombre de la carta");
            }

            if (entity.getFechaDesde() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la fecha de inicio");
            }

            if (entity.getFechaHasta() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la fecha de fin");
            }

            if (entity.getFechaHasta().isBefore(entity.getFechaDesde())) {
                throw new ErrorServiceException(
                        "La fecha de fin no puede ser menor a la fecha de inicio");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    public CartaDTO obtenerCartaActivaDTO() throws Exception {

        Carta carta = cartaRepository
                .findByActivoTrueAndEliminadoFalse()
                .orElseThrow(() -> new ErrorServiceException(
                        "No existe una carta activa"));

        return convertToDTO(carta);
    }

    @Transactional
    public void activarCarta(String id) throws Exception {

        Carta carta = findById(id);

        List<Carta> activas = cartaRepository.findAllActivas();

        for (Carta otra : activas) {
            if (!otra.getId().equals(carta.getId())) {
                otra.setActivo(false);
            }
        }

        carta.setActivo(true);

        cartaRepository.saveAll(activas);
        cartaRepository.save(carta);
    }

    public CartaDTO obtenerCartaDTO(String id) throws Exception {

        Carta carta = findById(id);

        return convertToDTO(carta);
    }

    @Transactional
    public Carta crearCarta(CartaDTO dto) throws Exception {

        Carta carta = new Carta();

        cargarDatosCarta(carta, dto);

        if (carta.isActivo()) {
            desactivarTodasLasActivas();
        }

        carta.setSecciones(
                construirSecciones(carta, dto));

        return save(carta);
    }

    @Transactional
    public Carta editarCarta(
            String id,
            CartaDTO dto) throws Exception {

        Carta carta = findById(id);

        cargarDatosCarta(carta, dto);

        validar(carta, "UPDATE");

        if (carta.isActivo()) {
            desactivarTodasLasActivasExcluyendo(id);
        }

        List<SeccionCarta> nuevasSecciones =
                construirSecciones(carta, dto);

        carta.getSecciones().clear();
        carta.getSecciones().addAll(nuevasSecciones);

        return cartaRepository.save(carta);
    }

    private void desactivarTodasLasActivas() {
        List<Carta> activas = cartaRepository.findAllActivas();
        for (Carta otra : activas) {
            otra.setActivo(false);
        }
        cartaRepository.saveAll(activas);
    }

    private void desactivarTodasLasActivasExcluyendo(String idExcluir) {
        List<Carta> activas = cartaRepository.findAllActivas();
        for (Carta otra : activas) {
            if (!otra.getId().equals(idExcluir)) {
                otra.setActivo(false);
            }
        }
        cartaRepository.saveAll(activas);
    }

    @Override
    @Transactional
    public boolean delete(String id) throws Exception {

        Carta carta = findById(id);

        if (carta.isEliminado()) {
            throw new ErrorServiceException(
                    "La carta ya fue eliminada");
        }

        carta.setEliminado(true);

        for (SeccionCarta seccion : carta.getSecciones()) {

            seccion.setEliminado(true);

            for (DetalleSeccionCarta detalle : seccion.getDetalles()) {

                detalle.setEliminado(true);
            }
        }

        cartaRepository.save(carta);

        return true;
    }

    public Page<CartaListadoDTO> obtenerListado(
            Pageable pageable) {

        Page<Carta> cartas =
                cartaRepository.findByEliminadoFalse(pageable);

        return cartas.map(c -> {

            CartaListadoDTO dto =
                    new CartaListadoDTO();

            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            dto.setFechaDesde(c.getFechaDesde());
            dto.setFechaHasta(c.getFechaHasta());
            dto.setActivo(c.isActivo());

            return dto;
        });
    }

    private CartaDTO convertToDTO(Carta carta) {

        CartaDTO dto = new CartaDTO();

        dto.setId(carta.getId());
        dto.setNombre(carta.getNombre());
        dto.setFechaDesde(carta.getFechaDesde());
        dto.setFechaHasta(carta.getFechaHasta());
        dto.setActivo(carta.isActivo());

        List<CategoriaDTO> categorias = new ArrayList<>();

        for (SeccionCarta seccion : carta.getSecciones()) {

            CategoriaDTO categoriaDTO = new CategoriaDTO();

            categoriaDTO.setId(
                    seccion.getCategoria().getId());

            categoriaDTO.setNombre(
                    seccion.getCategoria().getNombre());

            categoriaDTO.setOrden(seccion.getOrden());

            List<ArticuloCartaDTO> productos =
                    new ArrayList<>();

            List<MenuCartaDTO> menus = new ArrayList<>();

            for (DetalleSeccionCarta detalle :
                    seccion.getDetalles()) {

                if (detalle instanceof
                        DetalleSeccionCartaArticuloIndividual ai) {

                    ArticuloCartaDTO producto =
                            new ArticuloCartaDTO();

                    producto.setId(
                            ai.getArticulo().getId());

                    producto.setNombre(
                            ai.getArticulo().getNombre());

                    producto.setDescripcion(
                            ai.getArticulo().getDescripcion());

                    producto.setPrecio(
                            ai.getPrecio());

                    productos.add(producto);

                } else if (detalle instanceof
                        DetalleSeccionCartaMenu dscm
                        && dscm.getMenus() != null) {

                    for (Menu menu : dscm.getMenus()) {

                        MenuCartaDTO menuDTO =
                                new MenuCartaDTO();

                        menuDTO.setId(menu.getId());
                        menuDTO.setNombre(menu.getNombre());
                        menuDTO.setDescripcion(menu.getDescripcion());
                        menuDTO.setPrecio(menu.getPrecio());

                        menus.add(menuDTO);
                    }
                }
            }

            categoriaDTO.setProductos(productos);
            categoriaDTO.setMenus(menus);

            categorias.add(categoriaDTO);
        }

        dto.setCategorias(categorias);

        dto.getCategorias().sort(
                Comparator.comparingInt(CategoriaDTO::getOrden));

        return dto;
    }

    private void cargarDatosCarta(
            Carta carta,
            CartaDTO dto) {

        carta.setNombre(dto.getNombre());
        carta.setFechaDesde(dto.getFechaDesde());
        carta.setFechaHasta(dto.getFechaHasta());
        carta.setActivo(dto.isActivo());
    }

    private List<SeccionCarta> construirSecciones(
            Carta carta,
            CartaDTO dto) throws Exception {

        List<SeccionCarta> secciones = new ArrayList<>();

        if (dto.getCategorias() == null) {
            return secciones;
        }

        for (int i = 0; i < dto.getCategorias().size(); i++) {
            CategoriaDTO categoriaDTO = dto.getCategorias().get(i);

            if (categoriaDTO.getId() == null
                    || categoriaDTO.getId().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe seleccionar una categoría en la posición "
                                + (i + 1));
            }

            Categoria categoria;
            try {
                categoria = categoriaService.findById(categoriaDTO.getId());
            } catch (Exception e) {
                throw new ErrorServiceException(
                        "Categoría no encontrada (id: "
                                + categoriaDTO.getId() + ")");
            }

            SeccionCarta seccion = new SeccionCarta();
            seccion.setCategoria(categoria);
            seccion.setCarta(carta);
            seccion.setOrden(categoriaDTO.getOrden());

            List<DetalleSeccionCarta> detalles = new ArrayList<>();

            if (categoriaDTO.getProductos() != null) {
                for (int j = 0; j < categoriaDTO.getProductos().size(); j++) {
                    ArticuloCartaDTO articuloDTO =
                            categoriaDTO.getProductos().get(j);

                    if (articuloDTO.getId() == null
                            || articuloDTO.getId().isEmpty()) {
                        throw new ErrorServiceException(
                                "Debe seleccionar todos los artículos "
                                        + "(categoría " + (i + 1)
                                        + ", fila " + (j + 1) + ")");
                    }

                    Articulo articulo;
                    try {
                        articulo = articuloService.findById(
                                articuloDTO.getId());
                    } catch (Exception e) {
                        throw new ErrorServiceException(
                                "Artículo no encontrado (id: "
                                        + articuloDTO.getId() + ")");
                    }

                    DetalleSeccionCartaArticuloIndividual detalle =
                            new DetalleSeccionCartaArticuloIndividual();

                    detalle.setArticulo(articulo);
                    detalle.setPrecio(articuloDTO.getPrecio());
                    detalle.setSeccionCarta(seccion);

                    detalles.add(detalle);
                }
            }

            if (categoriaDTO.getMenus() != null) {
                for (int j = 0; j < categoriaDTO.getMenus().size(); j++) {
                    MenuCartaDTO menuDTO =
                            categoriaDTO.getMenus().get(j);

                    if (menuDTO.getId() == null
                            || menuDTO.getId().isEmpty()) {
                        throw new ErrorServiceException(
                                "Debe seleccionar todos los menús "
                                        + "(categoría " + (i + 1)
                                        + ", fila " + (j + 1) + ")");
                    }

                    Menu menu;
                    try {
                        menu = menuService.findById(menuDTO.getId());
                    } catch (Exception e) {
                        throw new ErrorServiceException(
                                "Menú no encontrado (id: "
                                        + menuDTO.getId() + ")");
                    }

                    DetalleSeccionCartaMenu detalle =
                            new DetalleSeccionCartaMenu();

                    List<Menu> menus = new ArrayList<>();
                    menus.add(menu);
                    detalle.setMenus(menus);
                    detalle.setSeccionCarta(seccion);

                    detalles.add(detalle);
                }
            }

            seccion.setDetalles(detalles);
            secciones.add(seccion);
        }

        return secciones;
    }


}
