package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Audited
public class DetalleComanda extends Base {

    private int cantidad;

    @Enumerated(EnumType.STRING)
    private EstadoDetalleComanda estadoDetalleComanda;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "comanda_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Comanda comanda;

    @ManyToOne
    @JoinColumn(name = "detalle_seccion_carta_id")
    private DetalleSeccionCarta detalleSeccionCarta;
}
