package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DetalleComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleComandaRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleFacturaRepository;
import com.apkrew.staffManagementServer.domain.repository.FacturaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleFacturaServiceImpl
        extends BaseServiceImpl<DetalleFactura, String>
        implements DetalleFacturaService {

    private final DetalleFacturaRepository detalleFacturaRepository;
    private final FacturaRepository facturaRepository;
    private final DetalleComandaRepository detalleComandaRepository;

    public DetalleFacturaServiceImpl(
            BaseRepository<DetalleFactura, String> baserepository,
            DetalleFacturaRepository detalleFacturaRepository,
            FacturaRepository facturaRepository,
            DetalleComandaRepository detalleComandaRepository) {

        super(baserepository);
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.facturaRepository = facturaRepository;
        this.detalleComandaRepository = detalleComandaRepository;
    }

    @Override
    @Transactional
    public DetalleFacturaResponseDTO saveFromDTO(DetalleFacturaRequestDTO dto) throws Exception {
        try {
            Factura factura = facturaRepository.findByIdAndEliminadoFalse(dto.getFacturaId())
                    .orElseThrow(() -> new ErrorServiceException("La factura indicada no existe"));

            List<DetalleComanda> detallesComanda = new ArrayList<>();
            if (dto.getDetallesComandaIds() != null) {
                for (String id : dto.getDetallesComandaIds()) {
                    DetalleComanda detalle = detalleComandaRepository.findByIdAndEliminadoFalse(id)
                            .orElseThrow(() -> new ErrorServiceException("El detalle de comanda con id " + id + " no existe"));
                    detallesComanda.add(detalle);
                }
            }

            DetalleFactura entity = DetalleFactura.builder()
                    .cantidad(dto.getCantidad())
                    .subtotal(dto.getSubtotal())
                    .factura(factura)
                    .detallesComanda(detallesComanda)
                    .build();

            validar(entity, "SAVE");
            entity = repository.save(entity);
            return convertToResponseDTO(entity);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error al guardar el detalle de factura");
        }
    }

    @Override
    @Transactional
    public DetalleFacturaResponseDTO updateFromDTO(String id, DetalleFacturaRequestDTO dto) throws Exception {
        try {
            DetalleFactura entity = detalleFacturaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("El detalle de factura no existe"));

            Factura factura = facturaRepository.findByIdAndEliminadoFalse(dto.getFacturaId())
                    .orElseThrow(() -> new ErrorServiceException("La factura indicada no existe"));

            List<DetalleComanda> detallesComanda = new ArrayList<>();
            if (dto.getDetallesComandaIds() != null) {
                for (String dcId : dto.getDetallesComandaIds()) {
                    DetalleComanda detalle = detalleComandaRepository.findByIdAndEliminadoFalse(dcId)
                            .orElseThrow(() -> new ErrorServiceException("El detalle de comanda con id " + dcId + " no existe"));
                    detallesComanda.add(detalle);
                }
            }

            entity.setCantidad(dto.getCantidad());
            entity.setSubtotal(dto.getSubtotal());
            entity.setFactura(factura);
            entity.getDetallesComanda().clear();
            entity.getDetallesComanda().addAll(detallesComanda);
            for (DetalleComanda dc : entity.getDetallesComanda()) {
                dc.setDetalleFactura(entity);
            }

            validar(entity, "UPDATE");
            entity = repository.save(entity);
            return convertToResponseDTO(entity);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error al actualizar el detalle de factura");
        }
    }

    @Override
    public DetalleFacturaResponseDTO convertToResponseDTO(DetalleFactura entity) {
        if (entity == null) return null;

        String comandaId = null;
        List<DetalleComandaResponseDTO> detallesComandaDTO = new ArrayList<>();
        if (entity.getDetallesComanda() != null) {
            for (DetalleComanda dc : entity.getDetallesComanda()) {
                if (dc.isEliminado()) continue;

                if (comandaId == null && dc.getComanda() != null) {
                    comandaId = dc.getComanda().getId();
                }

                String articuloNombre = "";
                if (dc.getDetalleSeccionCarta() != null) {
                    DetalleSeccionCarta detalleSeccionCarta = dc.getDetalleSeccionCarta();

                    if (detalleSeccionCarta instanceof DetalleSeccionCartaArticuloIndividual articuloIndividual && articuloIndividual.getArticulo() != null) {
                        articuloNombre = articuloIndividual.getArticulo().getNombre();
                    } else if (detalleSeccionCarta instanceof DetalleSeccionCartaMenu detalleSeccionCartaMenu && detalleSeccionCartaMenu.getMenus() != null && !detalleSeccionCartaMenu.getMenus().isEmpty()) {
                        List<String> nombres = new ArrayList<>();
                        for (Menu m : detalleSeccionCartaMenu.getMenus()) {
                            nombres.add(m.getNombre());
                        }
                        articuloNombre = String.join(", ", nombres);
                    }
                }

                detallesComandaDTO.add(DetalleComandaResponseDTO.builder()
                        .id(dc.getId())
                        .cantidad(dc.getCantidad())
                        .estadoDetalleComanda(dc.getEstadoDetalleComanda())
                        .subtotal(dc.getSubtotal())
                        .detalleSeccionCartaId(dc.getDetalleSeccionCarta() != null ? dc.getDetalleSeccionCarta().getId() : null)
                        .articuloNombre(articuloNombre)
                        .build());
            }
        }

        return DetalleFacturaResponseDTO.builder()
                .id(entity.getId())
                .cantidad(entity.getCantidad())
                .subtotal(entity.getSubtotal())
                .facturaId(entity.getFactura() != null ? entity.getFactura().getId() : null)
                .comandaId(comandaId)
                .eliminado(entity.isEliminado())
                .detallesComanda(detallesComandaDTO)
                .build();
    }

    @Override
    @Transactional
    public List<DetalleFacturaResponseDTO> findAllDTO() throws Exception {
        try {
            List<DetalleFactura> entities = repository.findByEliminadoFalse();
            List<DetalleFacturaResponseDTO> dtos = new ArrayList<>();
            for (DetalleFactura entity : entities) {
                dtos.add(convertToResponseDTO(entity));
            }
            return dtos;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener los detalles de factura");
        }
    }

    @Override
    @Transactional
    public Page<DetalleFacturaResponseDTO> findAllDTO(Pageable pageable) throws Exception {
        try {
            Page<DetalleFactura> entities = repository.findByEliminadoFalse(pageable);
            return entities.map(this::convertToResponseDTO);
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener los detalles de factura");
        }
    }

    @Override
    @Transactional
    public DetalleFacturaResponseDTO findByIdDTO(String id) throws Exception {
        try {
            DetalleFactura entity = detalleFacturaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("El detalle de factura no existe"));
            return convertToResponseDTO(entity);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener el detalle de factura");
        }
    }

    @Override
    public boolean validar(DetalleFactura entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getCantidad() <= 0) {

                throw new ErrorServiceException(
                        "La cantidad debe ser mayor a 0");
            }

            if (entity.getSubtotal() <= 0) {

                throw new ErrorServiceException(
                        "El subtotal debe ser mayor a 0");
            }

            if (entity.getFactura() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la factura");
            }

            if (entity.getDetallesComanda() == null || entity.getDetallesComanda().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar al menos un detalle de comanda");
            }

            return true;

        } catch (ErrorServiceException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
