package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ReciboDeSueldoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ReciboDeSueldoServiceImpl
        extends BaseServiceImpl<ReciboDeSueldo, String>
        implements ReciboDeSueldoService {

    private final ReciboDeSueldoRepository reciboDeSueldoRepository;

    public ReciboDeSueldoServiceImpl(
            BaseRepository<ReciboDeSueldo, String> baseRepository,
            ReciboDeSueldoRepository reciboDeSueldoRepository) {

        super(baseRepository);
        this.reciboDeSueldoRepository = reciboDeSueldoRepository;
    }

    @Override
    public boolean validar(ReciboDeSueldo entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getFechaDePago() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la fecha de pago");
            }

            if (entity.getTotalPago() <= 0) {
                throw new ErrorServiceException(
                        "El total de pago debe ser mayor a 0");
            }

            if (entity.getDetalles() == null ||
                    entity.getDetalles().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar al menos un detalle");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    public double calcularTotal(String id) throws Exception {
        try {
            ReciboDeSueldo recibo = findById(id);

            double total = recibo.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getValor())
                    .sum();

            recibo.setTotalPago(total);
            reciboDeSueldoRepository.save(recibo);

            return total;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
