package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Cliente;
import com.apkrew.staffManagementServer.domain.entity.Resenia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseniaRepository extends BaseRepository<Resenia, String> {
}
