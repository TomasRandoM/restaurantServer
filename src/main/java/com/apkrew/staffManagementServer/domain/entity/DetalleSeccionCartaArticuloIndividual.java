package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
public class DetalleSeccionCartaArticuloIndividual extends DetalleSeccionCarta {

    private double precio;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;
}
