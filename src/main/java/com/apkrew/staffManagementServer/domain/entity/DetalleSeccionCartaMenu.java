package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(
            name = "detalle_seccion_carta_menu_menu",
            joinColumns = @JoinColumn(name = "detalle_seccion_carta_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<Menu> menus;
}
