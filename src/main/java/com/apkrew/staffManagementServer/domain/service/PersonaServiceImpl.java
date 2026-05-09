package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Persona;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.PersonaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class PersonaServiceImpl extends BaseServiceImpl<Persona, String> implements PersonaService{

    private PersonaRepository personaRepository;

    public PersonaServiceImpl(BaseRepository<Persona,String> baseRepository, PersonaRepository personaRepository){
        super(baseRepository);
        this.personaRepository = personaRepository;
    }

    @Override
    public boolean validar(Persona entity, String caso) throws Exception {

        try {
            if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getApellido() == null || entity.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }

            if (entity.getDni() == null || entity.getDni().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el DNI");
            }

            if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el email");
            }

            if (entity.getFechaNacimiento() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de nacimiento");
            }

            if (entity.getUsuario() == null || entity.getUsuario().getId() == null) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            if (caso.equals("SAVE")) {

                if (personaRepository.existsByDniAndEliminadoFalse(entity.getDni())) {
                    throw new ErrorServiceException("Ya existe una persona con ese DNI");
                }

                if (personaRepository.existsByEmailAndEliminadoFalse(entity.getEmail())) {
                    throw new ErrorServiceException("Ya existe una persona con ese email");
                }

            } else {

                Persona persona = personaRepository
                        .findByDniAndEliminadoFalse(entity.getDni());

                if (persona != null && !persona.getId().equals(entity.getId())) {
                    throw new Exception("Ya existe una persona con ese DNI");
                }

                Persona personaEmail = personaRepository
                        .findByEmailAndEliminadoFalse(entity.getEmail());

                if (personaEmail != null && !personaEmail.getId().equals(entity.getId())) {
                    throw new Exception("Ya existe una persona con ese email");
                }
            }

            return true;

        }catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
