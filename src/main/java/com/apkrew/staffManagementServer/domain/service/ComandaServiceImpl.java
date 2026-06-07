package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ComandaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComandaServiceImpl extends BaseServiceImpl<Comanda, String> implements ComandaService {

    private final ComandaRepository comandaRepository;
    private final DetalleSeccionCartaService detalleSeccionCartaService;
    private final FacturaService facturaService;
    private final DetalleFacturaService detalleFacturaService;
    private final FormaDePagoService formaDePagoService;
    private final PromocionService promocionService;

    public ComandaServiceImpl(
            BaseRepository<Comanda, String> baseRepository,
            ComandaRepository comandaRepository,
            DetalleSeccionCartaService detalleSeccionCartaService,
            FacturaService facturaService,
            DetalleFacturaService detalleFacturaService,
            FormaDePagoService formaDePagoService,
            PromocionService promocionService) {
        super(baseRepository);
        this.comandaRepository = comandaRepository;
        this.detalleSeccionCartaService = detalleSeccionCartaService;
        this.facturaService = facturaService;
        this.detalleFacturaService = detalleFacturaService;
        this.formaDePagoService = formaDePagoService;
        this.promocionService = promocionService;
    }

    @Override
    @Transactional
    public ComandaResponseDTO saveFromDTO(ComandaRequestDTO dto) throws Exception {
        validarDTO(dto);

        Comanda comanda = Comanda.builder()
                .fechaSolicitudComanda(new Date())
                .estadoComanda(EstadoComanda.ABIERTA)
                .total(0.0)
                .detalles(new ArrayList<>())
                .build();

        double total = 0.0;

        if (dto.getDetalles() != null) {
            for (DetalleComandaRequestDTO detalleDTO : dto.getDetalles()) {
                DetalleSeccionCarta articuloInfo = detalleSeccionCartaService.findById(detalleDTO.getDetalleSeccionCartaId());
                double precioUnitario = obtenerPrecio(articuloInfo);

                DetalleComanda detalle = DetalleComanda.builder()
                        .cantidad(detalleDTO.getCantidad())
                        .subtotal(precioUnitario * detalleDTO.getCantidad())
                        .estadoDetalleComanda(EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                        .detalleSeccionCarta(articuloInfo)
                        .comanda(comanda)
                        .build();

                comanda.getDetalles().add(detalle);
                total += detalle.getSubtotal();
            }
        }

        comanda.setTotal(total);
        comanda = comandaRepository.save(comanda);

        return toResponseDTO(comanda);
    }

    @Override
    @Transactional
    public ComandaResponseDTO updateFromDTO(String id, ComandaRequestDTO dto) throws Exception {
        validarDTO(dto);

        Comanda comanda = comandaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ErrorServiceException("La comanda no existe"));

        if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA) {
            throw new ErrorServiceException("No se puede editar una comanda cerrada o anulada");
        }

        // Clear details safely to trigger orphan removal
        comanda.getDetalles().clear();
        double total = 0.0;

        if (dto.getDetalles() != null) {
            for (DetalleComandaRequestDTO detalleDTO : dto.getDetalles()) {
                DetalleSeccionCarta articuloInfo = detalleSeccionCartaService.findById(detalleDTO.getDetalleSeccionCartaId());
                double precioUnitario = obtenerPrecio(articuloInfo);

                DetalleComanda detalle = DetalleComanda.builder()
                        .cantidad(detalleDTO.getCantidad())
                        .subtotal(precioUnitario * detalleDTO.getCantidad())
                        .estadoDetalleComanda(detalleDTO.getEstadoDetalleComanda() != null ? detalleDTO.getEstadoDetalleComanda() : EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                        .detalleSeccionCarta(articuloInfo)
                        .comanda(comanda)
                        .build();

                comanda.getDetalles().add(detalle);
                total += detalle.getSubtotal();
            }
        }

        comanda.setTotal(total);
        comanda = comandaRepository.save(comanda);

        return toResponseDTO(comanda);
    }

    @Override
    @Transactional
    public ComandaResponseDTO agregarDetalleFromDTO(String comandaId, DetalleComandaRequestDTO detalleDto) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA) {
            throw new ErrorServiceException("No se pueden agregar detalles a una comanda cerrada o anulada.");
        }

        DetalleSeccionCarta articuloInfo = detalleSeccionCartaService.findById(detalleDto.getDetalleSeccionCartaId());
        double precioUnitario = obtenerPrecio(articuloInfo);

        DetalleComanda detalle = DetalleComanda.builder()
                .cantidad(detalleDto.getCantidad())
                .subtotal(precioUnitario * detalleDto.getCantidad())
                .estadoDetalleComanda(EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                .detalleSeccionCarta(articuloInfo)
                .comanda(comanda)
                .build();

        comanda.getDetalles().add(detalle);
        comanda.setTotal(comanda.getTotal() + detalle.getSubtotal());

        return toResponseDTO(comandaRepository.save(comanda));
    }

    @Override
    @Transactional
    public ComandaResponseDTO facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA) {
            throw new ErrorServiceException("La comanda no se encuentra abierta para ser facturada.");
        }
        if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
              throw new ErrorServiceException("La comanda no posee detalles para facturar.");
        }

        FormaDePago formaPago = formaDePagoService.findById(formaPagoId);
        Promocion promocion = null;
        if (promocionId != null && !promocionId.isBlank()) {
            promocion = promocionService.findById(promocionId);
        }

        Factura factura = new Factura();
        factura.setNumeroFactura(System.currentTimeMillis());
        factura.setFechaFactura(new Date());
        factura.setTotalPagado(comanda.getTotal());
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFormaPago(formaPago);
        factura.setPromocion(promocion);
        factura.setDetalles(new ArrayList<>());

        factura = facturaService.save(factura);

        List<DetalleFactura> detallesFactura = new ArrayList<>();
        for (DetalleComanda detComanda : comanda.getDetalles()) {
            DetalleFactura detFactura = new DetalleFactura();
            detFactura.setCantidad(detComanda.getCantidad());
            detFactura.setSubtotal(detComanda.getSubtotal());
            detFactura.setFactura(factura);
            detFactura = detalleFacturaService.save(detFactura);
            detallesFactura.add(detFactura);
        }
        factura.setDetalles(detallesFactura);

        comanda.setFactura(factura);
        comanda.setEstadoComanda(EstadoComanda.PENDIENTE_DE_ENTREGA);

        return toResponseDTO(comandaRepository.save(comanda));
    }

    @Override
    @Transactional
    public ComandaResponseDTO entregarComanda(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra pendiente de entrega.");
        }
        comanda.setEstadoComanda(EstadoComanda.FINALIZADA);
        comanda.setFechaEntregaComanda(new Date());
        return toResponseDTO(comandaRepository.save(comanda));
    }

    @Override
    @Transactional
    public ComandaResponseDTO marcarEntregaFallida(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra pendiente de entrega.");
        }
        comanda.setEstadoComanda(EstadoComanda.ENTREGA_FALLIDA);
        return toResponseDTO(comandaRepository.save(comanda));
    }

    @Override
    @Transactional
    public ComandaResponseDTO anularComanda(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA && comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra en un estado que permita anulación.");
        }
        comanda.setEstadoComanda(EstadoComanda.ANULADA);
        return toResponseDTO(comandaRepository.save(comanda));
    }

    @Override
    @Transactional
    public ComandaResponseDTO findResponseById(String id) throws Exception {
        return toResponseDTO(findById(id));
    }

    @Override
    @Transactional
    public List<ComandaResponseDTO> findAllResponse() throws Exception {
        List<Comanda> comandas = findAll();
        List<ComandaResponseDTO> dtos = new ArrayList<>();
        for (Comanda c : comandas) {
            dtos.add(toResponseDTO(c));
        }
        return dtos;
    }

    //aplica paginado
    @Override
    @Transactional
    public org.springframework.data.domain.Page<ComandaResponseDTO> findAllResponse(org.springframework.data.domain.Pageable pageable) throws Exception {
        org.springframework.data.domain.Page<Comanda> comandas = findAll(pageable);
        return comandas.map(this::toResponseDTO);
    }

    //implementacion necesaria porque esta  en la interfaz, la verdadera validacion se hace en validarDTO
    @Override
    public boolean validar(Comanda entity, String caso) throws ErrorServiceException {
        return true;
    }

    private void validarDTO(ComandaRequestDTO dto) throws ErrorServiceException {
        if (dto.getDetalles() != null) {
            for (DetalleComandaRequestDTO det : dto.getDetalles()) {
                if (det.getCantidad() == null || det.getCantidad() <= 0) {
                    throw new ErrorServiceException("La cantidad de los detalles debe ser mayor a 0");
                }
                if (det.getDetalleSeccionCartaId() == null || det.getDetalleSeccionCartaId().isBlank()) {
                    throw new ErrorServiceException("Debe especificar un artículo válido para el detalle");
                }
            }
        }
    }

    private double obtenerPrecio(DetalleSeccionCarta articuloInfo) throws ErrorServiceException {
        if (articuloInfo instanceof DetalleSeccionCartaArticuloIndividual) {
            return ((DetalleSeccionCartaArticuloIndividual) articuloInfo).getPrecio();
        } else {
            throw new ErrorServiceException("Detalle de sección de carta no soportado.");
        }
    }

    private ComandaResponseDTO toResponseDTO(Comanda comanda) {
        ComandaResponseDTO dto = ComandaResponseDTO.builder()
                .id(comanda.getId())
                .fechaSolicitudComanda(comanda.getFechaSolicitudComanda())
                .fechaEntregaComanda(comanda.getFechaEntregaComanda())
                .estadoComanda(comanda.getEstadoComanda())
                .total(comanda.getTotal())
                .detalles(new ArrayList<>())
                .build();

        if (comanda.getFactura() != null) {
            dto.setFacturaId(comanda.getFactura().getId());
            dto.setFacturaNumero(comanda.getFactura().getNumeroFactura());
        }

        if (comanda.getDetalles() != null) {
            for (DetalleComanda det : comanda.getDetalles()) {
                String articuloNombre = "Artículo de Carta";
                if (det.getDetalleSeccionCarta() instanceof DetalleSeccionCartaArticuloIndividual) {
                    articuloNombre = ((DetalleSeccionCartaArticuloIndividual) det.getDetalleSeccionCarta()).getArticulo().getNombre();
                }

                DetalleComandaResponseDTO detDTO = DetalleComandaResponseDTO.builder()
                        .id(det.getId())
                        .cantidad(det.getCantidad())
                        .subtotal(det.getSubtotal())
                        .estadoDetalleComanda(det.getEstadoDetalleComanda())
                        .detalleSeccionCartaId(det.getDetalleSeccionCarta().getId())
                        .articuloNombre(articuloNombre)
                        .build();
                dto.getDetalles().add(detDTO);
            }
        }
        return dto;
    }
}
