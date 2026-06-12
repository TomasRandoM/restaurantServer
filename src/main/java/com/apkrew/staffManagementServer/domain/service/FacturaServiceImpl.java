package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DetalleComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.FacturaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.FacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.repository.*;
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
    private final ComandaRepository comandaRepository;
    private final FormaDePagoRepository formaDePagoRepository;
    private final PromocionRepository promocionRepository;

    public FacturaServiceImpl(
            BaseRepository<Factura, String> baserepository,
            FacturaRepository facturaRepository,
            ComandaRepository comandaRepository,
            FormaDePagoRepository formaDePagoRepository,
            PromocionRepository promocionRepository) {

        super(baserepository);
        this.facturaRepository = facturaRepository;
        this.comandaRepository = comandaRepository;
        this.formaDePagoRepository = formaDePagoRepository;
        this.promocionRepository = promocionRepository;
    }

    @Override
    @Transactional
    public FacturaResponseDTO saveFromDTO(FacturaRequestDTO dto) throws Exception {
        try {
            FormaDePago formaPago = formaDePagoRepository.findByIdAndEliminadoFalse(dto.getFormaPagoId())
                    .orElseThrow(() -> new ErrorServiceException("La forma de pago indicada no existe"));

            Promocion promocion = null;
            if (dto.getPromocionId() != null && !dto.getPromocionId().isBlank()) {
                promocion = promocionRepository.findByIdAndEliminadoFalse(dto.getPromocionId())
                        .orElseThrow(() -> new ErrorServiceException("La promocion indicada no existe"));
            }

            Factura entity = Factura.builder()
                    .numeroFactura(dto.getNumeroFactura())
                    .fechaFactura(dto.getFechaFactura())
                    .totalPagado(dto.getTotalPagado())
                    .estado(dto.getEstado())
                    .formaPago(formaPago)
                    .promocion(promocion)
                    .build();

            List<DetalleFactura> detalles = buildDetallesFromComandas(dto.getComandaIds(), entity);
            entity.setDetalles(detalles);

            validar(entity, "SAVE");
            entity = repository.save(entity);
            return convertToResponseDTO(entity);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error al guardar la factura");
        }
    }

    @Override
    @Transactional
    public FacturaResponseDTO updateFromDTO(String id, FacturaRequestDTO dto) throws Exception {
        try {
            Factura entity = facturaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("La factura no existe"));

            FormaDePago formaPago = formaDePagoRepository.findByIdAndEliminadoFalse(dto.getFormaPagoId())
                    .orElseThrow(() -> new ErrorServiceException("La forma de pago indicada no existe"));

            Promocion promocion = null;
            if (dto.getPromocionId() != null && !dto.getPromocionId().isBlank()) {
                promocion = promocionRepository.findByIdAndEliminadoFalse(dto.getPromocionId())
                        .orElseThrow(() -> new ErrorServiceException("La promocion indicada no existe"));
            }

            entity.setNumeroFactura(dto.getNumeroFactura());
            entity.setFechaFactura(dto.getFechaFactura());
            entity.setTotalPagado(dto.getTotalPagado());
            entity.setEstado(dto.getEstado());
            entity.setFormaPago(formaPago);
            entity.setPromocion(promocion);

            List<DetalleFactura> nuevosDetalles = buildDetallesFromComandas(dto.getComandaIds(), entity);
            entity.getDetalles().clear();
            entity.getDetalles().addAll(nuevosDetalles);

            validar(entity, "UPDATE");
            entity = repository.save(entity);
            return convertToResponseDTO(entity);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error al actualizar la factura");
        }
    }

    private List<DetalleFactura> buildDetallesFromComandas(List<String> comandaIds, Factura factura) throws ErrorServiceException {
        List<DetalleFactura> detalles = new ArrayList<>();

        if (comandaIds == null || comandaIds.isEmpty()) {
            throw new ErrorServiceException("Debe indicar al menos una comanda");
        }

        for (String comandaId : comandaIds) {
            Comanda comanda = comandaRepository.findByIdAndEliminadoFalse(comandaId)
                    .orElseThrow(() -> new ErrorServiceException("La comanda con id " + comandaId + " no existe"));

            if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
                throw new ErrorServiceException("La comanda " + comandaId + " no tiene detalles");
            }

            List<DetalleComanda> detallesComanda = new ArrayList<>();
            double subtotal = 0;
            for (DetalleComanda dc : comanda.getDetalles()) {
                if (!dc.isEliminado()) {
                    detallesComanda.add(dc);
                    subtotal += dc.getSubtotal();
                }
            }

            if (detallesComanda.isEmpty()) {
                throw new ErrorServiceException("La comanda " + comandaId + " no tiene detalles activos");
            }

            DetalleFactura detalleFactura = DetalleFactura.builder()
                    .cantidad(detallesComanda.size())
                    .subtotal(subtotal)
                    .factura(factura)
                    .detallesComanda(detallesComanda)
                    .build();

            for (DetalleComanda dc : detallesComanda) {
                dc.setDetalleFactura(detalleFactura);
            }

            detalles.add(detalleFactura);
        }

        return detalles;
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
    public boolean validar(Factura entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getNumeroFactura() == null) {

                throw new ErrorServiceException(
                        "Debe indicar el numero de factura");
            }

            if (entity.getFechaFactura() == null) {

                throw new ErrorServiceException(
                        "Debe indicar la fecha de factura");
            }

            if (entity.getTotalPagado() <= 0) {

                throw new ErrorServiceException(
                        "El total pagado debe ser mayor a 0");
            }

            if (entity.getEstado() == null) {

                throw new ErrorServiceException(
                        "Debe indicar el estado de la factura");
            }

            if (entity.getFormaPago() == null) {

                throw new ErrorServiceException(
                        "Debe indicar la forma de pago");
            }

            if (entity.getDetalles() == null ||
                    entity.getDetalles().isEmpty()) {

                throw new ErrorServiceException(
                        "Debe indicar al menos un detalle");
            }

            if (caso.equals("SAVE")) {

                if (facturaRepository
                        .existsByNumeroFacturaAndEliminadoFalse(
                                entity.getNumeroFactura())) {

                    throw new ErrorServiceException(
                            "La factura ya existe en el sistema");
                }

            } else {

                Factura factura =
                        facturaRepository
                                .findByNumeroFacturaAndEliminadoFalse(
                                        entity.getNumeroFactura());

                if (factura != null) {

                    if (!factura.getId().equals(entity.getId())) {

                        throw new ErrorServiceException(
                                "La factura especificada ya existe en el sistema");
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
}
