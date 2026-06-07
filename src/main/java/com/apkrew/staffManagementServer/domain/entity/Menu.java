package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private double precio;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleMenu> detalles;
}
