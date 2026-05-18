package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.EstadoRegistroHorario;
import com.apkrew.staffManagementServer.domain.entity.Empleado;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Audited
public class RegistroHorario extends Base {
    private Date fechaEntrada;
    private Date fechaSalida;
    private String observacion;
    private EstadoRegistroHorario estadoRegistroHorario;
    @ManyToOne
    private Empleado empleado;
}
