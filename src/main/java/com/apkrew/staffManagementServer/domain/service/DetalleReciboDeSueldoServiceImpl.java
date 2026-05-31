package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleReciboDeSueldoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DetalleReciboDeSueldoServiceImpl
        extends BaseServiceImpl<DetalleReciboDeSueldo, String>
        implements DetalleReciboDeSueldoService {

    private final DetalleReciboDeSueldoRepository detalleReciboDeSueldoRepository;

    public DetalleReciboDeSueldoServiceImpl(
            BaseRepository<DetalleReciboDeSueldo, String> baseRepository,
            DetalleReciboDeSueldoRepository detalleReciboDeSueldoRepository) {

        super(baseRepository);
        this.detalleReciboDeSueldoRepository = detalleReciboDeSueldoRepository;
    }

    @Override
    public boolean validar(DetalleReciboDeSueldo entity, String caso) throws ErrorServiceException {

        try {

            if (entity.getCantidad() == null || entity.getCantidad() <= 0) {
                throw new ErrorServiceException(
                        "La cantidad debe ser mayor a 0");
            }

            if (entity.getValor() == null || entity.getValor() <= 0) {
                throw new ErrorServiceException(
                        "El valor debe ser mayor a 0");
            }

            if (entity.getTipoDetalleRecibo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el tipo de detalle");
            }

            if (entity.getReciboDeSueldo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el recibo de sueldo");
            }

            if (entity.getItemReciboDeSueldo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el item de recibo de sueldo");
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
