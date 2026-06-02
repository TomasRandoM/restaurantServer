package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.SeccionCarta;
import org.springframework.stereotype.Repository;

@Repository
public interface SeccionCartaRepository
        extends BaseRepository<SeccionCarta, String> {
    boolean existsByCartaIdAndCategoriaIdAndEliminadoFalse(
            String cartaId,
            String categoriaId
    );
    SeccionCarta findByCartaIdAndCategoriaIdAndEliminadoFalse(
            String cartaId,
            String categoriaId
    );
}
