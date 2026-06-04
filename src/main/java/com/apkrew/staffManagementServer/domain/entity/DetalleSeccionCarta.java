package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public abstract class DetalleSeccionCarta extends Base {

    @ManyToOne
    @JoinColumn(name = "seccion_carta_id")
    private SeccionCarta seccionCarta;
}
