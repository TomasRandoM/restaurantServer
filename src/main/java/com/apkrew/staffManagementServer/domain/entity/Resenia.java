package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class Resenia extends Base{
    @Column(columnDefinition = "TEXT")
    private String observacion;

    private LocalDateTime fechaResenia;

    @ManyToOne
    private Cliente cliente;
}
