package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ComandaRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaArticuloIndividualRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaMenuRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComandaServiceImpl extends BaseServiceImpl<Comanda, String> implements ComandaService {

    private final ComandaRepository comandaRepository;
    private final DetalleSeccionCartaArticuloIndividualRepository articuloIndividualRepository;
    private final DetalleSeccionCartaMenuRepository detalleSeccionCartaMenuRepository;
    private final FacturaService facturaService;
    private final DetalleFacturaService detalleFacturaService;
    private final FormaDePagoService formaDePagoService;
    private final PromocionService promocionService;

    public ComandaServiceImpl(
            BaseRepository<Comanda, String> baseRepository,
            ComandaRepository comandaRepository,
            DetalleSeccionCartaArticuloIndividualRepository articuloIndividualRepository,
            DetalleSeccionCartaMenuRepository detalleSeccionCartaMenuRepository,
            FacturaService facturaService,
            DetalleFacturaService detalleFacturaService,
            FormaDePagoService formaDePagoService,
            PromocionService promocionService) {
        super(baseRepository);
        this.comandaRepository = comandaRepository;
        this.articuloIndividualRepository = articuloIndividualRepository;
        this.detalleSeccionCartaMenuRepository = detalleSeccionCartaMenuRepository;
        this.facturaService = facturaService;
        this.detalleFacturaService = detalleFacturaService;
        this.formaDePagoService = formaDePagoService;
        this.promocionService = promocionService;
    }

    @Override
    @Transactional
    public Comanda save(Comanda entity) throws Exception {
        try {
            validar(entity, "SAVE");

            Comanda comanda = Comanda.builder()
                    .fechaSolicitudComanda(entity.getFechaSolicitudComanda() != null ? entity.getFechaSolicitudComanda() : new Date())
                    .estadoComanda(entity.getEstadoComanda() != null ? entity.getEstadoComanda() : EstadoComanda.ABIERTA)
                    .total(0.0)
                    .detalles(new ArrayList<>())
                    .build();

            double total = 0.0;
            if (entity.getDetalles() != null) {
                for (DetalleComanda det : entity.getDetalles()) {
                    DetalleSeccionCarta articuloInfo = obtenerDetalleSeccionCarta(det.getDetalleSeccionCarta().getId());
                    double precioUnitario = obtenerPrecio(articuloInfo);

                    DetalleComanda detalle = DetalleComanda.builder()
                            .cantidad(det.getCantidad())
                            .subtotal(precioUnitario * det.getCantidad())
                            .estadoDetalleComanda(det.getEstadoDetalleComanda() != null ? det.getEstadoDetalleComanda() : EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                            .detalleSeccionCarta(articuloInfo)
                            .comanda(comanda)
                            .build();

                    comanda.getDetalles().add(detalle);
                    total += detalle.getSubtotal();
                }
            }
            comanda.setTotal(total);

            return comandaRepository.save(comanda);
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
            validar(entity, "UPDATE");
            Comanda comandaAModificar = comandaRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("La comanda no existe"));

            if (comandaAModificar.getEstadoComanda() != EstadoComanda.ABIERTA) {
                throw new ErrorServiceException("No se puede editar una comanda cerrada o anulada");
            }

            // modifico los datos de la comanda existente
            comandaAModificar.setFechaSolicitudComanda(entity.getFechaSolicitudComanda() != null ? entity.getFechaSolicitudComanda() : comandaAModificar.getFechaSolicitudComanda());
            comandaAModificar.setFechaEntregaComanda(entity.getFechaEntregaComanda());
            comandaAModificar.setEstadoComanda(entity.getEstadoComanda() != null ? entity.getEstadoComanda() : comandaAModificar.getEstadoComanda());
            comandaAModificar.setFactura(entity.getFactura());

            // Clear details safely to trigger orphan removal
            comandaAModificar.getDetalles().clear();

            double total = 0.0;
            if (entity.getDetalles() != null) {
                for (DetalleComanda det : entity.getDetalles()) {
                    DetalleSeccionCarta articuloInfo = obtenerDetalleSeccionCarta(det.getDetalleSeccionCarta().getId());
                    double precioUnitario = obtenerPrecio(articuloInfo);

                    DetalleComanda detalle = DetalleComanda.builder()
                            .cantidad(det.getCantidad())
                            .subtotal(precioUnitario * det.getCantidad())
                            .estadoDetalleComanda(det.getEstadoDetalleComanda() != null ? det.getEstadoDetalleComanda() : EstadoDetalleComanda.EN_PROCESO_DE_SOLICITUD)
                            .detalleSeccionCarta(articuloInfo)
                            .comanda(comandaAModificar)
                            .build();

                    comandaAModificar.getDetalles().add(detalle);
                    total += detalle.getSubtotal();
                }
            }
            comandaAModificar.setTotal(total);

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

            if (comandaElim.getEstadoComanda() != EstadoComanda.ANULADA){
                throw new ErrorServiceException("la comanda debe encontrarse anulada para poder eliminarse");
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

    @Override
    @Transactional
    public Comanda facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception {
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

        List<DetalleFactura> detallesFactura = new ArrayList<>();
        for (DetalleComanda detComanda : comanda.getDetalles()) {
            DetalleFactura detFactura = new DetalleFactura();
            detFactura.setCantidad(detComanda.getCantidad());
            detFactura.setSubtotal(detComanda.getSubtotal());
            detFactura.setFactura(factura);
            detallesFactura.add(detFactura);
        }
        factura.setDetalles(detallesFactura);

        // Se guarda la factura que ahora contiene los detalles requeridos para la validación
        factura = facturaService.save(factura);

        // Se guardan los detalles individuales
        for (DetalleFactura detFactura : detallesFactura) {
            detalleFacturaService.save(detFactura);
        }

        comanda.setFactura(factura);
        comanda.setEstadoComanda(EstadoComanda.PENDIENTE_DE_ENTREGA);

        return comandaRepository.save(comanda);
    }
    ///----Cambio los estados de la comanda----///
    @Override
    @Transactional
    public Comanda entregarComanda(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra pendiente de entrega.");
        }
        comanda.setEstadoComanda(EstadoComanda.FINALIZADA);
        comanda.setFechaEntregaComanda(new Date());
        return comandaRepository.save(comanda);
    }

    @Override
    @Transactional
    public Comanda marcarEntregaFallida(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra pendiente de entrega.");
        }
        comanda.setEstadoComanda(EstadoComanda.ENTREGA_FALLIDA);
        return comandaRepository.save(comanda);
    }

    @Override
    @Transactional
    public Comanda anularComanda(String comandaId) throws Exception {
        Comanda comanda = findById(comandaId);
        if (comanda.getEstadoComanda() != EstadoComanda.ABIERTA && comanda.getEstadoComanda() != EstadoComanda.PENDIENTE_DE_ENTREGA) {
            throw new ErrorServiceException("La comanda no se encuentra en un estado que permita anulación.");
        }
        comanda.setEstadoComanda(EstadoComanda.ANULADA);
        return comandaRepository.save(comanda);
    }
    ///----Fin de cambios de estado comanda----///



    @Override
    public boolean validar(Comanda entity, String caso) throws ErrorServiceException {
        if (entity.getDetalles() != null) {
            for (DetalleComanda det : entity.getDetalles()) {
                if (det.getCantidad() <= 0) {
                    throw new ErrorServiceException("La cantidad de los detalles debe ser mayor a 0");
                }
                if (det.getDetalleSeccionCarta() == null || det.getDetalleSeccionCarta().getId() == null || det.getDetalleSeccionCarta().getId().isBlank()) {
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
}
