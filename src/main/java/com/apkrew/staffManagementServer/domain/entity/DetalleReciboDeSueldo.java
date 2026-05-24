package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.TipoDetalleReciboSueldo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Audited
public class DetalleReciboDeSueldo extends Base {

    private int cantidad;

    private double valor;

    @Enumerated(EnumType.STRING)
    private TipoDetalleReciboSueldo tipoDetalleRecibo;

    @ManyToOne
    @JoinColumn(name = "recibo_de_sueldo_id")
    private ReciboDeSueldo reciboDeSueldo;

    @ManyToOne
    @JoinColumn(name = "item_recibo_de_sueldo_id")
    private ItemReciboDeSueldo itemReciboDeSueldo;
}
