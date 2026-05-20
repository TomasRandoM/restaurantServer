package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleFactura;
import com.apkrew.staffManagementServer.domain.entity.Factura;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.FacturaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

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
