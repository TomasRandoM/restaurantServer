package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Audited
public class ReciboDeSueldo extends Base {

    private Date fechaDePago;

    private int mesPago;

    private double totalPago;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "reciboDeSueldo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReciboDeSueldo> detalles;
}
