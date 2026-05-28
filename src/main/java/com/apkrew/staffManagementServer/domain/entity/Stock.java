package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties("stocks")
    private Articulo articulo;
}
