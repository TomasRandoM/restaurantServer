package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.RegistroHorarioRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class RegistroHorarioServiceImpl extends BaseServiceImpl<RegistroHorario, String> implements RegistroHorarioService {

    private final RegistroHorarioRepository registroHorarioRepository;

    public RegistroHorarioServiceImpl(BaseRepository<RegistroHorario, String> baseRepository,
                                      RegistroHorarioRepository registroHorarioRepository) {
        super(baseRepository);
        this.registroHorarioRepository = registroHorarioRepository;
    }

    @Override
    public boolean validar(RegistroHorario entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("El registro horario no puede ser nulo");
            }

            if (entity.getEmpleado() == null || entity.getEmpleado().getId() == null) {
                throw new ErrorServiceException("Debe indicar el empleado");
            }

            if (entity.getFechaEntrada() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de entrada");
            }

            if (entity.getEstadoRegistroHorario() == null) {
                throw new ErrorServiceException("Debe indicar el estado del registro horario");
            }

            if (entity.getFechaSalida() != null && entity.getFechaSalida().before(entity.getFechaEntrada())) {
                throw new ErrorServiceException("La fecha de salida no puede ser anterior a la fecha de entrada");
            }

            if (caso.equals("SAVE")) {
                if (registroHorarioRepository.existsByEmpleadoIdAndFechaEntradaAndEliminadoFalse(entity.getEmpleado().getId(), entity.getFechaEntrada())) {
                    throw new ErrorServiceException("El empleado ya tiene un registro horario abierto");
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
