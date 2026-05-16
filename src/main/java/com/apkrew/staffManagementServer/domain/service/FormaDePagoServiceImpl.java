package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.FormaDePago;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.FormaDePagoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;

public class FormaDePagoServiceImpl extends BaseServiceImpl<FormaDePago,String> implements FormaDePagoService{

    private final FormaDePagoRepository formaDePagoRepository;

    public FormaDePagoServiceImpl(BaseRepository<FormaDePago, String> baseRepository , FormaDePagoRepository formaDePagoRepository) {
        super(baseRepository);
        this.formaDePagoRepository = formaDePagoRepository;
    }

    @Override
    public boolean validar(FormaDePago entity, String caso) throws ErrorServiceException {
        try {

            if (entity == null) {
                throw new ErrorServiceException("Debe indicar la forma de pago");
            }

            if (entity.getMetodoPago() == null) {
                throw new ErrorServiceException("Debe indicar el método de pago");
            }

            if (entity.getObservacion() != null &&
                    entity.getObservacion().length() > 255) {
                throw new ErrorServiceException("La observación no puede superar los 255 caracteres");
            }

            if (caso.equals("SAVE")) {

                if (formaDePagoRepository
                        .existsByMetodoPagoAndEliminadoFalse(entity.getMetodoPago())) {

                    throw new ErrorServiceException(
                            "La forma de pago ya existe en el sistema"
                    );
                }

            } else {

                FormaDePago fp = formaDePagoRepository
                        .findByMetodoPagoAndEliminadoFalse(entity.getMetodoPago());

                if (fp != null) {

                    if (!fp.getId().equals(entity.getId())) {

                        throw new ErrorServiceException(
                                "La forma de pago especificada ya existe en el sistema"
                        );
                    }
                }
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
