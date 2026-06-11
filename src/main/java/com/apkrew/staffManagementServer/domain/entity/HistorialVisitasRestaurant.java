package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.service.ClienteServiceImpl;
import jakarta.persistence.Entity;
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
public class HistorialVisitasRestaurant extends Base {
    int cantidadVisita;

    @ManyToOne
    private Cliente cliente;
}
