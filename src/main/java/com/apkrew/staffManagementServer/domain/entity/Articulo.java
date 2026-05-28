package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.util.List;

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

    @OneToMany(mappedBy = "articulo")
    @JsonIgnoreProperties("articulo")
    @ToString.Exclude
    private List<Stock> stocks;
}
