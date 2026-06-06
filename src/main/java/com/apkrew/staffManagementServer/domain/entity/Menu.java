package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Menu extends Base {

    private String nombre;

    private double precio;

    @ManyToOne
    @JoinColumn(name = "detalle_seccion_carta_menu_id", nullable = true)
    private DetalleSeccionCartaMenu detalleSeccionCartaMenu;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleMenu> detalles;
}
