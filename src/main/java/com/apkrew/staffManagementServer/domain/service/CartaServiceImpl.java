package com.apkrew.staffManagementServer.domain.service;


import com.apkrew.staffManagementServer.domain.dto.ArticuloCartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CartaListadoDTO;
import com.apkrew.staffManagementServer.domain.dto.CategoriaDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.CartaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartaServiceImpl extends BaseServiceImpl<Carta, String>
        implements CartaService {

    private final CartaRepository cartaRepository;

    private final CategoriaService categoriaService;
    private final ArticuloService articuloService;

    public CartaServiceImpl(
            BaseRepository<Carta, String> baserepository,
            CartaRepository cartaRepository,
            CategoriaService categoriaService,
            ArticuloService articuloService) {

        super(baserepository);

        this.cartaRepository = cartaRepository;
        this.categoriaService = categoriaService;
        this.articuloService = articuloService;
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

            List<Carta> cartasSolapadas =
                    cartaRepository.buscarCartasSolapadas(
                            entity.getFechaDesde(),
                            entity.getFechaHasta());

            if (caso.equals("SAVE")) {

                if (!cartasSolapadas.isEmpty()) {
                    throw new ErrorServiceException(
                            "Ya existe una carta para ese período");
                }

            } else {

                for (Carta carta : cartasSolapadas) {

                    if (!carta.getId().equals(entity.getId())) {

                        throw new ErrorServiceException(
                                "Ya existe una carta para ese período");
                    }
                }
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

        LocalDate hoy = LocalDate.now();

        Carta carta = cartaRepository
                .findByFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualAndEliminadoFalse(
                        hoy,
                        hoy)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ErrorServiceException(
                        "No existe una carta activa"));

        CartaDTO dto = new CartaDTO();

        dto.setId(carta.getId());
        dto.setFechaDesde(carta.getFechaDesde());
        dto.setFechaHasta(carta.getFechaHasta());

        List<CategoriaDTO> categorias = new ArrayList<>();

        for (SeccionCarta seccion : carta.getSecciones()) {

            CategoriaDTO categoriaDTO = new CategoriaDTO();

            categoriaDTO.setId(
                    seccion.getCategoria().getId());

            categoriaDTO.setNombre(
                    seccion.getCategoria().getNombre());

            List<ArticuloCartaDTO> productos =
                    new ArrayList<>();

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
                }
            }

            categoriaDTO.setProductos(productos);

            categorias.add(categoriaDTO);
        }

        dto.setCategorias(categorias);

        return dto;
    }

    @Transactional
    public Carta crearCarta(CartaDTO dto) throws Exception {

        Carta carta = new Carta();

        carta.setNombre(dto.getNombre());
        carta.setFechaDesde(dto.getFechaDesde());
        carta.setFechaHasta(dto.getFechaHasta());

        List<SeccionCarta> secciones = new ArrayList<>();

        for (CategoriaDTO categoriaDTO : dto.getCategorias()) {

            SeccionCarta seccion = new SeccionCarta();

            Categoria categoria =
                    categoriaService.findById(categoriaDTO.getId());

            seccion.setCategoria(categoria);
            seccion.setCarta(carta);

            List<DetalleSeccionCarta> detalles = new ArrayList<>();

            for (ArticuloCartaDTO articuloDTO : categoriaDTO.getProductos()) {

                Articulo articulo =
                        articuloService.findById(articuloDTO.getId());

                DetalleSeccionCartaArticuloIndividual detalle =
                        new DetalleSeccionCartaArticuloIndividual();

                detalle.setArticulo(articulo);
                detalle.setPrecio(articuloDTO.getPrecio());
                detalle.setSeccionCarta(seccion);

                detalles.add(detalle);
            }

            seccion.setDetalles(detalles);

            secciones.add(seccion);
        }

        carta.setSecciones(secciones);

        return save(carta);
    }

    public List<CartaListadoDTO> obtenerListado() {

        List<Carta> cartas = cartaRepository.findAll();

        return cartas.stream()
                .map(c -> {
                    CartaListadoDTO dto =
                            new CartaListadoDTO();

                    dto.setId(c.getId());
                    dto.setNombre(c.getNombre());
                    dto.setFechaDesde(c.getFechaDesde());
                    dto.setFechaHasta(c.getFechaHasta());

                    return dto;
                })
                .toList();
    }
}
