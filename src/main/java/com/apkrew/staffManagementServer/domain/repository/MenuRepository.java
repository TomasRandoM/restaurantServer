package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository
        extends BaseRepository<Menu, String> {
}
