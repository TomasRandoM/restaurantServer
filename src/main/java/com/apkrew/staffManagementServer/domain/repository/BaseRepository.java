package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Base;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository <E extends Base, ID extends Serializable> extends JpaRepository<E, ID> {
    List<E> findByEliminadoFalse();

    Optional<E> findByIdAndEliminadoFalse(ID id);

    Page<E> findByEliminadoFalse(Pageable pageable);

    boolean existsByIdAndEliminadoFalse(ID id);
}