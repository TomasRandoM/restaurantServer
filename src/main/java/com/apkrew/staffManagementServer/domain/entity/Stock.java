package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class Stock extends Base{
    private double minimo;
    private double cantidadActual;
    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;
}
