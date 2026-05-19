package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Column;
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
public class Articulo extends Base{
    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private boolean sinTAC;
    private boolean esIngrediente;
    @ManyToOne
    @JoinColumn(name = "unidad_de_medida_id")
    private UnidadDeMedida unidadDeMedida;
}
