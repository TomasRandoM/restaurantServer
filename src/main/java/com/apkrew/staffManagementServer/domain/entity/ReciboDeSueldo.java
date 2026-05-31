package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.entity.Empleado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private Date fechaDePago;

    private Integer mesPago;

    private Double totalPago;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "reciboDeSueldo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"reciboDeSueldo"})
    private List<DetalleReciboDeSueldo> detalles;
}
