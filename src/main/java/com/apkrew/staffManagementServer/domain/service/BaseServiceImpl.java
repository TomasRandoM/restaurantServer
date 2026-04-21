package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Base;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<E extends Base, ID extends Serializable> implements BaseService<E, ID> {

    protected BaseRepository<E, ID> repository;

    public BaseServiceImpl(BaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<E> findAll() throws Exception {
        try {
            List<E> entities = repository.findByEliminadoFalse();
            return entities;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al obtener la lista de elementos");
        }
    }


    @Override
    @Transactional
    public Page<E> findAll(Pageable pageable) throws Exception{
        try {
            Page<E> entities = repository.findByEliminadoFalse(pageable);
            return entities;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al buscar los resultados");
        }
    }

    @Override
    @Transactional
    public E findById(ID id) throws Exception {
        try {
            Optional<E> entity = repository.findByIdAndEliminadoFalse(id);
            return entity.get();
        } catch (Exception e) {
            throw new ErrorServiceException("La entidad con el id indicado no existe");
        }
    }


    @Override
    @Transactional
    public E save(E entity) throws Exception {
        try {
            validar(entity, "SAVE");
            entity = repository.save(entity);
            return entity;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al guardar la entidad");
        }
    }

    @Override
    @Transactional
    public E update(ID id, E entity) throws Exception {
        try {
            validar(entity, "UPDATE");
            Optional<E> entityOptional = repository.findByIdAndEliminadoFalse(id);
            E entityUpdate= entityOptional.get();
            entityUpdate = repository.save(entity);
            return entityUpdate;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("La entidad con el id ingresado no existe");
        }
    }

    @Override
    @Transactional
    public boolean delete(ID id) throws Exception {
        try {
            if (repository.existsByIdAndEliminadoFalse(id)) {
                Optional<E> optional = repository.findByIdAndEliminadoFalse(id);
                E entity = optional.get();
                entity.setEliminado(true);
                repository.save(entity);
                return true;
            } else {
                throw new ErrorServiceException("El objeto ya fue eliminado o no existe");
            }
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception("Hubo un problema eliminando el objeto");
        }
    }
}
