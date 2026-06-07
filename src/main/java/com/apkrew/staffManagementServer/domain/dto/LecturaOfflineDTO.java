package com.apkrew.staffManagementServer.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LecturaOfflineDTO {
    private String empleadoId;
    private Date fecha;
}
