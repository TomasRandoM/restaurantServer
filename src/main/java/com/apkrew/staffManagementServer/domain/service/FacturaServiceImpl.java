package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DetalleComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.FacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.FacturaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaServiceImpl
        extends BaseServiceImpl<Factura, String>
        implements FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaServiceImpl(
            BaseRepository<Factura, String> baserepository,
            FacturaRepository facturaRepository) {

        super(baserepository);
        this.facturaRepository = facturaRepository;
    }

    @Override
    public FacturaResponseDTO convertToResponseDTO(Factura entity) {
        if (entity == null) return null;

        List<DetalleFacturaResponseDTO> detallesDTO = new ArrayList<>();
        if (entity.getDetalles() != null) {
            for (DetalleFactura df : entity.getDetalles()) {
                if (df.isEliminado()) continue;

                String comandaId = null;
                List<DetalleComandaResponseDTO> detallesComandaDTO = new ArrayList<>();
                if (df.getDetallesComanda() != null) {
                    for (DetalleComanda dc : df.getDetallesComanda()) {
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

                detallesDTO.add(DetalleFacturaResponseDTO.builder()
                        .id(df.getId())
                        .cantidad(df.getCantidad())
                        .subtotal(df.getSubtotal())
                        .facturaId(entity.getId())
                        .comandaId(comandaId)
                        .eliminado(df.isEliminado())
                        .detallesComanda(detallesComandaDTO)
                        .build());
            }
        }

        return FacturaResponseDTO.builder()
                .id(entity.getId())
                .numeroFactura(entity.getNumeroFactura())
                .fechaFactura(entity.getFechaFactura())
                .totalPagado(entity.getTotalPagado())
                .estado(entity.getEstado())
                .formaPagoId(entity.getFormaPago() != null ? entity.getFormaPago().getId() : null)
                .promocionId(entity.getPromocion() != null ? entity.getPromocion().getId() : null)
                .eliminado(entity.isEliminado())
                .detalles(detallesDTO)
                .build();
    }

    @Override
    @Transactional
    public List<FacturaResponseDTO> findAllDTO() throws Exception {
        try {
            List<Factura> entities = repository.findByEliminadoFalse();
            List<FacturaResponseDTO> dtos = new ArrayList<>();
            for (Factura entity : entities) {
                dtos.add(convertToResponseDTO(entity));
            }
            return dtos;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener las facturas");
        }
    }

    @Override
    @Transactional
    public Page<FacturaResponseDTO> findAllDTO(Pageable pageable) throws Exception {
        try {
            Page<Factura> entities = repository.findByEliminadoFalse(pageable);
            return entities.map(this::convertToResponseDTO);
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener las facturas");
        }
    }

    @Override
    @Transactional
    public FacturaResponseDTO findByIdDTO(String id) throws Exception {
        try {
            Factura entity = facturaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("La factura no existe"));
            return convertToResponseDTO(entity);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener la factura");
        }
    }

    @Override
    public boolean validar(Factura entity, String caso) throws ErrorServiceException {
        return true;
    }
}
