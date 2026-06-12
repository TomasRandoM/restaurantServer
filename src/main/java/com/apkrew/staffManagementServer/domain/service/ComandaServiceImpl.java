package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleComandaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ComandaRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaArticuloIndividualRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaMenuRepository;
import com.apkrew.staffManagementServer.domain.repository.FacturaRepository;
import com.apkrew.staffManagementServer.domain.repository.FormaDePagoRepository;
import com.apkrew.staffManagementServer.domain.repository.PromocionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComandaServiceImpl extends BaseServiceImpl<Comanda, String> implements ComandaService {

    private final ComandaRepository comandaRepository;
    private final DetalleSeccionCartaArticuloIndividualRepository articuloIndividualRepository;
    private final DetalleSeccionCartaMenuRepository detalleSeccionCartaMenuRepository;
    private final ClienteServiceImpl clienteService;
    private final FacturaRepository facturaRepository;
    private final FormaDePagoRepository formaDePagoRepository;
    private final PromocionRepository promocionRepository;

    public ComandaServiceImpl(
            BaseRepository<Comanda, String> baseRepository,
            ComandaRepository comandaRepository,
            DetalleSeccionCartaArticuloIndividualRepository articuloIndividualRepository,
            DetalleSeccionCartaMenuRepository detalleSeccionCartaMenuRepository,
            ClienteServiceImpl clienteService,
            FacturaRepository facturaRepository,
            FormaDePagoRepository formaDePagoRepository,
            PromocionRepository promocionRepository) {
        super(baseRepository);
        this.comandaRepository = comandaRepository;
        this.articuloIndividualRepository = articuloIndividualRepository;
        this.detalleSeccionCartaMenuRepository = detalleSeccionCartaMenuRepository;
        this.clienteService = clienteService;
        this.facturaRepository = facturaRepository;
        this.formaDePagoRepository = formaDePagoRepository;
        this.promocionRepository = promocionRepository;
    }

    @Override
    @Transactional
    public Comanda save(Comanda entity) throws Exception {
        try {
            if (entity.getEstadoComanda() == null) {
                entity.setEstadoComanda(EstadoComanda.ABIERTA);
            }
            if (entity.getFechaSolicitudComanda() == null) {
                entity.setFechaSolicitudComanda(LocalDateTime.now()); ///Correccion de Fecha
            }

            //aca va a tirar un error? porque va a tratar de traer el cliente completo, cuando yo solo le estoy pasando un id
            Cliente clienteComanda = clienteService.findById(entity.getCliente().getId());

            if (clienteComanda != null){
                entity.setCliente(clienteComanda);
            }

            if (entity.getDetalles() != null) {
                for (DetalleComanda det : entity.getDetalles()) {
                    String cardId = det.getDetalleSeccionCarta() != null ? 
                            det.getDetalleSeccionCarta().getId() : 
                            det.getDetalleSeccionCartaId();

                    DetalleSeccionCarta articuloInfo = obtenerDetalleSeccionCarta(cardId);
                    validarPerteneceACartaActiva(articuloInfo);
                    double precioUnitario = obtenerPrecio(articuloInfo);

                    det.setDetalleSeccionCarta(articuloInfo);
                    det.setSubtotal(precioUnitario * det.getCantidad());
                    if (det.getEstadoDetalleComanda() == null) {
                        det.setEstadoDetalleComanda(EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD);
                    }
                    det.setComanda(entity);
                }
            }

            validar(entity, "SAVE");
            return comandaRepository.save(entity);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al guardar la comanda");
        }
    }

    @Override
    @Transactional
    public Comanda update(String id, Comanda entity) throws Exception {
        try {
            if (entity.getDetalles() != null) {
                for (DetalleComanda det : entity.getDetalles()) {
                    String cardId = det.getDetalleSeccionCarta() != null ? 
                            det.getDetalleSeccionCarta().getId() : 
                            det.getDetalleSeccionCartaId();
                    DetalleSeccionCarta articuloInfo = obtenerDetalleSeccionCarta(cardId);
                    validarPerteneceACartaActiva(articuloInfo);
                    det.setDetalleSeccionCarta(articuloInfo);
                }
            }

            validar(entity, "UPDATE");

            Comanda comandaAModificar = comandaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("La comanda no existe"));

            if (comandaAModificar.getEstadoComanda() == EstadoComanda.FINALIZADA
                    || comandaAModificar.getEstadoComanda() == EstadoComanda.ANULADA) {
                throw new ErrorServiceException("No se puede editar una comanda finalizada o anulada");
            }

            Cliente clienteModificado = clienteService.findById(entity.getCliente().getId());

            if (clienteModificado == null){
                throw new ErrorServiceException("El cliente no existe");
            }

            if (!comandaAModificar.getCliente().getId().equals(clienteModificado.getId())) {
                comandaAModificar.setCliente(clienteModificado); //si cambia de cliente, seteo el cliente nuevo
            }


            // modifico los datos de la comanda existente
            comandaAModificar.setFechaSolicitudComanda(entity.getFechaSolicitudComanda() != null ? entity.getFechaSolicitudComanda() : comandaAModificar.getFechaSolicitudComanda());
            comandaAModificar.setFechaEntregaComanda(entity.getFechaEntregaComanda());

            comandaAModificar.setEstadoComanda(entity.getEstadoComanda() != null ? entity.getEstadoComanda() : comandaAModificar.getEstadoComanda());

            comandaAModificar.getDetalles().clear();

            if (entity.getDetalles() != null) {
                for (DetalleComanda det : entity.getDetalles()) {
                    String cardId = det.getDetalleSeccionCarta() != null ? 
                            det.getDetalleSeccionCarta().getId() : 
                            det.getDetalleSeccionCartaId();
                    DetalleSeccionCarta articuloInfo = obtenerDetalleSeccionCarta(cardId);
                    validarPerteneceACartaActiva(articuloInfo);
                    double precioUnitario = obtenerPrecio(articuloInfo);

                    DetalleComanda detalle = DetalleComanda.builder()
                            .cantidad(det.getCantidad())
                            .subtotal(precioUnitario * det.getCantidad())
                            .estadoDetalleComanda(det.getEstadoDetalleComanda() != null ? det.getEstadoDetalleComanda() : EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                            .detalleSeccionCarta(articuloInfo)
                            .comanda(comandaAModificar)
                            .build();

                    comandaAModificar.getDetalles().add(detalle);
                }
            }


            return comandaRepository.save(comandaAModificar); //guardo la comanda con los datos cambiados
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al actualizar la comanda");
        }
    }

    @Override
    @Transactional
    public boolean delete(String comandaId) throws Exception{
        try {
            Comanda comandaElim = findById(comandaId);

            if (comandaElim == null){
                throw new ErrorServiceException("la comanda no existe");
            }

            if (comandaElim.getEstadoComanda() != EstadoComanda.ANULADA) {
                if (comandaElim.getEstadoComanda() == EstadoComanda.ABIERTA
                        || comandaElim.getEstadoComanda() == EstadoComanda.PENDIENTE_DE_ENTREGA
                        || comandaElim.getEstadoComanda() == EstadoComanda.ENTREGA_FALLIDA) {
                    comandaElim.setEstadoComanda(EstadoComanda.ANULADA);
                } else {
                    throw new ErrorServiceException("No se puede anular una comanda en estado " + comandaElim.getEstadoComanda());
                }
            }

            List<DetalleComanda> detallesElim = comandaElim.getDetalles();
            for (DetalleComanda detalle : detallesElim) {
                detalle.setEliminado(true);
            }

            comandaElim.setEliminado(true);
            comandaRepository.save(comandaElim);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al eliminar la comanda");
        }
    }

    ///----Cambio los estados de la comanda----///
    @Override
    @Transactional
    public Comanda anularComanda(String comandaId) throws Exception {
        try {
            Comanda comanda = comandaRepository.findByIdAndEliminadoFalse(comandaId)
                    .orElseThrow(() -> new ErrorServiceException("La comanda no existe"));

            if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA
                    && comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA
                    && comanda.getEstadoComanda() != EstadoComanda.ENTREGA_FALLIDA) {
                throw new ErrorServiceException("La comanda no se encuentra en un estado que permita anulación.");
            }

            if (comanda.getFacturaNumero() == null) {
                List<DetalleComanda> detallesActivos = new ArrayList<>();
                double total = 0;
                if (comanda.getDetalles() != null) {
                    for (DetalleComanda dc : comanda.getDetalles()) {
                        if (!dc.isEliminado()) {
                            detallesActivos.add(dc);
                            total += dc.getSubtotal();
                        }
                    }
                }

                Long maxNumero = facturaRepository.findMaxNumeroFactura();
                Long nextNumero = maxNumero + 1;

                Factura factura = Factura.builder()
                        .numeroFactura(nextNumero)
                        .fechaFactura(new Date())
                        .totalPagado(0)
                        .estado(EstadoFactura.ANULADA)
                        .build();

                if (!detallesActivos.isEmpty()) {
                    DetalleFactura detalleFactura = DetalleFactura.builder()
                            .cantidad(detallesActivos.size())
                            .subtotal(total)
                            .factura(factura)
                            .detallesComanda(detallesActivos)
                            .build();

                    for (DetalleComanda dc : detallesActivos) {
                        dc.setDetalleFactura(detalleFactura);
                    }

                    factura.setDetalles(List.of(detalleFactura));
                }

                comanda.setFacturaNumero(nextNumero);
                facturaRepository.save(factura);
            }

            comanda.setEstadoComanda(EstadoComanda.ANULADA);
            return comandaRepository.save(comanda);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al anular la comanda");
        }
    }

    @Override
    @Transactional
    public ComandaResponseDTO facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception {
        try {
            Comanda comanda = comandaRepository.findByIdAndEliminadoFalse(comandaId)
                    .orElseThrow(() -> new ErrorServiceException("La comanda no existe"));

            if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA) {
                throw new ErrorServiceException("Solo se pueden facturar comandas en estado ABIERTA");
            }

            FormaDePago formaPago = formaDePagoRepository.findByIdAndEliminadoFalse(formaPagoId)
                    .orElseThrow(() -> new ErrorServiceException("La forma de pago indicada no existe"));

            Promocion promocion = null;
            if (promocionId != null && !promocionId.isBlank()) {
                promocion = promocionRepository.findByIdAndEliminadoFalse(promocionId)
                        .orElseThrow(() -> new ErrorServiceException("La promocion indicada no existe"));
            }

            List<DetalleComanda> detallesActivos = new ArrayList<>();
            double totalSinDescuento = 0;
            if (comanda.getDetalles() != null) {
                for (DetalleComanda dc : comanda.getDetalles()) {
                    if (!dc.isEliminado()) {
                        detallesActivos.add(dc);
                        totalSinDescuento += dc.getSubtotal();
                    }
                }
            }

            if (detallesActivos.isEmpty()) {
                throw new ErrorServiceException("La comanda no tiene detalles activos para facturar");
            }

            Long maxNumero = facturaRepository.findMaxNumeroFactura();
            Long nextNumero = maxNumero + 1;

            double totalPagado = totalSinDescuento;
            if (promocion != null) {
                double descuento = totalSinDescuento * (promocion.getPorcentajeDescuento() / 100.0);
                totalPagado -= descuento;
            }

            Factura factura = Factura.builder()
                    .numeroFactura(nextNumero)
                    .fechaFactura(new Date())
                    .totalPagado(totalPagado)
                    .estado(EstadoFactura.PAGADA)
                    .formaPago(formaPago)
                    .promocion(promocion)
                    .build();

            DetalleFactura detalleFactura = DetalleFactura.builder()
                    .cantidad(detallesActivos.size())
                    .subtotal(totalSinDescuento)
                    .factura(factura)
                    .detallesComanda(detallesActivos)
                    .build();

            for (DetalleComanda dc : detallesActivos) {
                dc.setDetalleFactura(detalleFactura);
            }

            factura.setDetalles(List.of(detalleFactura));

            comanda.setEstadoComanda(EstadoComanda.FINALIZADA);
            comanda.setFechaEntregaComanda(LocalDateTime.now());
            comanda.setFacturaNumero(nextNumero);

            facturaRepository.save(factura);
            comandaRepository.save(comanda);

            return convertToResponseDTO(comanda);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al facturar la comanda");
        }
    }
    ///----Fin de cambios de estado comanda----///


    //valido que cantidad de articulos  sea mayor a 0 y que sean validos
    @Override
    public boolean validar(Comanda entity, String caso) throws ErrorServiceException {
        if (entity.getDetalles() != null) {
            for (DetalleComanda det : entity.getDetalles()) {
                if (det.getCantidad() <= 0) {
                    throw new ErrorServiceException("La cantidad de los detalles debe ser mayor a 0");
                }
                String cardId = det.getDetalleSeccionCarta() != null ? 
                        det.getDetalleSeccionCarta().getId() : 
                        det.getDetalleSeccionCartaId();
                if (cardId == null || cardId.isBlank()) {
                    throw new ErrorServiceException("Debe especificar un artículo válido para el detalle");
                }
            }
        }
        return true;
    }

    //Obtengo los datos de los articulos de la carta, sean individuales o de un Menu

    private DetalleSeccionCarta obtenerDetalleSeccionCarta(String id) throws ErrorServiceException {
        DetalleSeccionCarta articuloInfo = articuloIndividualRepository.findByIdAndEliminadoFalse(id)
                .map(x -> (DetalleSeccionCarta) x)
                .orElse(null);

        if (articuloInfo == null) {
            articuloInfo = detalleSeccionCartaMenuRepository.findByIdAndEliminadoFalse(id)
                    .map(x -> (DetalleSeccionCarta) x)
                    .orElse(null);
        }

        if (articuloInfo == null) {
            throw new ErrorServiceException("El detalle de la sección de la carta no existe.");
        }
        return articuloInfo;
    }

    private double obtenerPrecio(DetalleSeccionCarta articuloInfo) throws ErrorServiceException {
        if (articuloInfo instanceof DetalleSeccionCartaArticuloIndividual) {
            return ((DetalleSeccionCartaArticuloIndividual) articuloInfo).getPrecio();
        } else if (articuloInfo instanceof DetalleSeccionCartaMenu) {
            List<Menu> menus = ((DetalleSeccionCartaMenu) articuloInfo).getMenus(); //lista los articulos pertenecientes al menu
            if (menus == null || menus.isEmpty()) {
                throw new ErrorServiceException("El menú de la sección de la carta no contiene platos asociados.");
            }
            return menus.stream().mapToDouble(Menu::getPrecio).sum();
        } else {
            throw new ErrorServiceException("Detalle de sección de carta no soportado.");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ComandaResponseDTO obtenerComandaDTO(String id) throws Exception {
        Comanda comanda = findById(id);
        if (comanda == null) {
            throw new ErrorServiceException("La comanda no existe");
        }
        return convertToResponseDTO(comanda);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComandaResponseDTO> obtenerComandasDTO(Pageable pageable) throws Exception {
        Page<Comanda> comandas = findAll(pageable);
        return comandas.map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComandaResponseDTO> obtenerComandasDTO() throws Exception {
        List<Comanda> comandas = findAll();
        List<ComandaResponseDTO> dtos = new ArrayList<>();
        for (Comanda c : comandas) {
            dtos.add(convertToResponseDTO(c));
        }
        return dtos;
    }

    @Override
    public ComandaResponseDTO convertToResponseDTO(Comanda comanda) {
        if (comanda == null) return null;

        List<DetalleComandaResponseDTO> detallesDTO = new ArrayList<>();
        double totalComanda = 0.0;
        if (comanda.getDetalles() != null) {
            for (DetalleComanda det : comanda.getDetalles()) {
                if (det.isEliminado()) continue;

                String articuloNombre = "";
                if (det.getDetalleSeccionCarta() != null) {
                    DetalleSeccionCarta detalleSeccionCarta = det.getDetalleSeccionCarta();

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

                double sub = det.getSubtotal();
                totalComanda += sub;

                DetalleComandaResponseDTO detDTO = DetalleComandaResponseDTO.builder()
                        .id(det.getId())
                        .cantidad(det.getCantidad())
                        .estadoDetalleComanda(det.getEstadoDetalleComanda())
                        .subtotal(sub)
                        .detalleSeccionCartaId(det.getDetalleSeccionCarta() != null ? det.getDetalleSeccionCarta().getId() : null)
                        .articuloNombre(articuloNombre)
                        .build();
                detallesDTO.add(detDTO);
            }
        }

        ComandaResponseDTO.ComandaResponseDTOBuilder builder = ComandaResponseDTO.builder()
                .id(comanda.getId())
                .fechaSolicitudComanda(comanda.getFechaSolicitudComanda())
                .fechaEntregaComanda(comanda.getFechaEntregaComanda())
                .estadoComanda(comanda.getEstadoComanda())
                .facturaNumero(comanda.getFacturaNumero())
                .total(totalComanda)
                .detalles(detallesDTO);

        if (comanda.getCliente() != null) {
            builder.clienteId(comanda.getCliente().getId());
            builder.clienteNombre(comanda.getCliente().getNombre() + " " + comanda.getCliente().getApellido());
        }

        return builder.build();
    }

    private void validarPerteneceACartaActiva(DetalleSeccionCarta detalle) throws ErrorServiceException {
        if (detalle == null) return;
        if (detalle.getSeccionCarta() == null) return;
        if (detalle.getSeccionCarta().getCarta() == null) return;
        if (!detalle.getSeccionCarta().getCarta().isActivo()) {
            throw new ErrorServiceException("El ítem seleccionado no pertenece a la carta activa");
        }
    }
}
