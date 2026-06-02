package com.apkrew.staffManagementServer.domain.service;


import com.apkrew.staffManagementServer.domain.dto.ArticuloCartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CartaDTO;
import com.apkrew.staffManagementServer.domain.dto.CategoriaDTO;
import com.apkrew.staffManagementServer.domain.entity.Carta;
import com.apkrew.staffManagementServer.domain.entity.DetalleSeccionCarta;
import com.apkrew.staffManagementServer.domain.entity.DetalleSeccionCartaArticuloIndividual;
import com.apkrew.staffManagementServer.domain.entity.SeccionCarta;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.CartaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartaServiceImpl extends BaseServiceImpl<Carta, String>
        implements CartaService {

    private final CartaRepository cartaRepository;

    public CartaServiceImpl(
            BaseRepository<Carta, String> baserepository,
            CartaRepository cartaRepository) {

        super(baserepository);
        this.cartaRepository = cartaRepository;
    }

    @Override
    public boolean validar(Carta entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getFechaDesde() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de inicio");
            }

            if (entity.getFechaHasta() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de fin");
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
}
