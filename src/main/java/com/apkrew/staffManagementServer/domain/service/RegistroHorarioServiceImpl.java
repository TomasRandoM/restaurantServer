package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.LecturaOfflineDTO;
import com.apkrew.staffManagementServer.domain.entity.Empleado;
import com.apkrew.staffManagementServer.domain.entity.QRSecrets;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.enums.EstadoRegistroHorario;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.RegistroHorarioRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistroHorarioServiceImpl extends BaseServiceImpl<RegistroHorario, String> implements RegistroHorarioService {

    private final RegistroHorarioRepository registroHorarioRepository;
    private final EmpleadoService empleadoService;
    public RegistroHorarioServiceImpl(BaseRepository<RegistroHorario, String> baseRepository,
                                      RegistroHorarioRepository registroHorarioRepository,
                                      EmpleadoService empleadoService) {
        super(baseRepository);
        this.registroHorarioRepository = registroHorarioRepository;
        this.empleadoService = empleadoService;
    }

    @Scheduled(cron = "0 0 4 * * *")
    protected void createRegisters() throws ErrorServiceException {
        try {
            // Nuevo estado por si algún empleado no marca su salida (PERO si su entrada)
            List<RegistroHorario> olvidados = registroHorarioRepository.findAllByEstadoRegistroHorarioAndEliminadoFalse(EstadoRegistroHorario.PRESENTE);
            for (RegistroHorario r : olvidados) {
                r.setEstadoRegistroHorario(EstadoRegistroHorario.SALIDA_NO_REGISTRADA);
                registroHorarioRepository.save(r);
            }

            List<Empleado> empleados = empleadoService.findAll();
            RegistroHorario registroHorario;
            Date today = Date.from(
                    LocalDate.now()
                            .atTime(LocalTime.MAX)
                            .atZone(ZoneId.of("America/Argentina/Mendoza"))
                            .toInstant()
            );
            for (Empleado e : empleados) {
                registroHorario = new RegistroHorario();
                registroHorario.setEmpleado(e);
                registroHorario.setEstadoRegistroHorario(EstadoRegistroHorario.AUSENTE);
                registroHorario.setObservacion("");
                registroHorario.setFechaEntrada(null);
                registroHorario.setFechaSalida(today);
                registroHorarioRepository.save(registroHorario);
            }
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public void checkRegister(String empleadoId) throws ErrorServiceException {
        ZoneId zona = ZoneId.of("America/Argentina/Mendoza");
        LocalDate hoy = LocalDate.now(zona);

        Optional<RegistroHorario> presente = registroHorarioRepository.findFirstByEmpleadoIdAndEstadoRegistroHorarioAndEliminadoFalseOrderByFechaSalidaDesc(empleadoId, EstadoRegistroHorario.PRESENTE);

        if (presente.isPresent()) {
            RegistroHorario registro = presente.get();
            registro.setFechaSalida(new Date());
            registro.setEstadoRegistroHorario(EstadoRegistroHorario.RETIRADO);
            registroHorarioRepository.save(registro);
            return;
        }

        Optional<RegistroHorario> ausente = registroHorarioRepository.findFirstByEmpleadoIdAndEstadoRegistroHorarioAndEliminadoFalseOrderByFechaSalidaDesc(empleadoId, EstadoRegistroHorario.AUSENTE);

        if (ausente.isPresent()) {
            RegistroHorario registro = ausente.get();
            LocalDate fechaRegistro = registro.getFechaSalida().toInstant().atZone(zona).toLocalDate();
            if (fechaRegistro.equals(hoy) || fechaRegistro.equals(hoy.minusDays(1))) {
                registro.setFechaEntrada(new Date());
                registro.setEstadoRegistroHorario(EstadoRegistroHorario.PRESENTE);
                registroHorarioRepository.save(registro);
                return;
            }
        }

        throw new ErrorServiceException("No hay registro horario abierto para este empleado");
    }

    @Transactional
    public List<RegistroHorario> sincronizarLecturasOffline(List<LecturaOfflineDTO> lecturas) throws Exception {
        try {
            if (lecturas == null || lecturas.isEmpty()) {
                throw new ErrorServiceException("La lista de lecturas no puede ser nula ni vacía");
            }

            for (LecturaOfflineDTO lectura : lecturas) {
                if (lectura == null || lectura.getEmpleadoId() == null || lectura.getEmpleadoId().isBlank()) {
                    throw new ErrorServiceException("Cada lectura debe indicar el empleado");
                }
                if (lectura.getFecha() == null) {
                    throw new ErrorServiceException("Cada lectura debe indicar la fecha");
                }
            }

            List<LecturaOfflineDTO> ordenadas = new ArrayList<>(lecturas);
            ordenadas.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));

            ZoneId zona = ZoneId.of("America/Argentina/Mendoza");
            List<RegistroHorario> actualizados = new ArrayList<>();

            for (LecturaOfflineDTO lectura : ordenadas) {
                LocalDate businessDay = lectura.getFecha().toInstant().atZone(zona).minusHours(4).toLocalDate();
                Date desde = Date.from(businessDay.atStartOfDay(zona).toInstant());
                Date hasta = Date.from(businessDay.atTime(LocalTime.MAX).atZone(zona).toInstant());

                List<RegistroHorario> encontrados = registroHorarioRepository
                        .findByEmpleadoIdAndFechaSalidaBetweenAndEliminadoFalse(lectura.getEmpleadoId(), desde, hasta);

                if (encontrados.isEmpty()) {
                    throw new ErrorServiceException("No existe registro pre-creado para el empleado " + lectura.getEmpleadoId() + " en el día " + businessDay);
                }
                if (encontrados.size() > 1) {
                    throw new ErrorServiceException("Inconsistencia: múltiples registros para el empleado " + lectura.getEmpleadoId() + " en el día " + businessDay);
                }

                RegistroHorario r = encontrados.get(0);

                switch (r.getEstadoRegistroHorario()) {
                    case AUSENTE:
                        r.setFechaEntrada(lectura.getFecha());
                        r.setEstadoRegistroHorario(EstadoRegistroHorario.PRESENTE);
                        break;
                    case PRESENTE, SALIDA_NO_REGISTRADA:
                        if (r.getFechaEntrada() != null && lectura.getFecha().before(r.getFechaEntrada())) {
                            throw new ErrorServiceException("La salida no puede ser anterior a la entrada");
                        }
                        r.setFechaSalida(lectura.getFecha());
                        r.setEstadoRegistroHorario(EstadoRegistroHorario.RETIRADO);
                        break;
                    case RETIRADO:
                        throw new ErrorServiceException("El día ya está cerrado o llegaron más de 2 lecturas para el empleado " + lectura.getEmpleadoId());
                    default:
                        throw new ErrorServiceException("Estado de registro horario no válido para el empleado " + lectura.getEmpleadoId());
                }

                actualizados.add(registroHorarioRepository.save(r));
            }

            return actualizados;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
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
