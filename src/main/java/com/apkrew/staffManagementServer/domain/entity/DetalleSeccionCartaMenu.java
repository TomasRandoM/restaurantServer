package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
public class DetalleSeccionCartaMenu extends DetalleSeccionCarta {

    @OneToMany(mappedBy = "detalleSeccionCartaMenu")
    private List<Menu> menus;
}
