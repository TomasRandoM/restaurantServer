package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Pais;
import com.apkrew.staffManagementServer.domain.entity.Usuario;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.PaisRepository;
import com.apkrew.staffManagementServer.domain.repository.UsuarioRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario,String> implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(BaseRepository<Usuario, String> baserepository, UsuarioRepository usuarioRepository) {
        super(baserepository);
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Usuario save(Usuario entity) throws Exception {
        validar(entity, "SAVE");
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        return super.save(entity);
    }

    @Override
    public Usuario changePassword(Usuario entity) throws Exception {
        validar(entity, "UPDATE");
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        return super.save(entity);
    }

    public Usuario searchByEmail(String email) throws Exception {
        Usuario usuario = usuarioRepository.findByEmailAndEliminadoFalse(email);
        return usuario;
    }

    @Override
    public boolean validar(Usuario entity, String caso) throws Exception {
        try {
            if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getPersona() == null) {
                throw new ErrorServiceException("Debe indicar una persona asociada");
            }

            if (caso.equals("SAVE")) {
                if (usuarioRepository.existsByEmailAndEliminadoFalse(entity.getEmail())) {
                    throw new ErrorServiceException("El usuario ya existe en el sistema");
                }
            } else {
                Usuario cc = usuarioRepository.findByEmailAndEliminadoFalse(entity.getEmail());
                if (cc != null) {
                    if (!cc.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("El usuario especificado ya existe en el sistema");
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
