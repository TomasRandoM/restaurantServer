package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.entity.Pais;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentacionRepository extends BaseRepository<Documentacion, String> {

}