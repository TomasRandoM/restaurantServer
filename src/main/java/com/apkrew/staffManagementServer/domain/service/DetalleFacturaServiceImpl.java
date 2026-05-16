package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleFactura;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleFacturaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DetalleFacturaServiceImpl
        extends BaseServiceImpl<DetalleFactura, String>
        implements DetalleFacturaService {

    private final DetalleFacturaRepository detalleFacturaRepository;

    public DetalleFacturaServiceImpl(
            BaseRepository<DetalleFactura, String> baserepository,
            DetalleFacturaRepository detalleFacturaRepository) {

        super(baserepository);
        this.detalleFacturaRepository = detalleFacturaRepository;
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

            return true;

        } catch (ErrorServiceException ex) {

            throw ex;

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
