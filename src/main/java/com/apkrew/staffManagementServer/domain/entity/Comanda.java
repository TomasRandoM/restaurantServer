package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mysql.cj.xdevapi.Client;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Audited
public class Comanda extends Base {

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitudComanda;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntregaComanda;

    @Enumerated(EnumType.STRING)
    private EstadoComanda estadoComanda;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "cliente_id")
//    private Cliente cliente;

    private double total;

    @Builder.Default
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("comanda")
    private List<DetalleComanda> detalles = new ArrayList<>();

    @NotAudited
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    @JsonIgnoreProperties("detalles")
    private Factura factura;
}
