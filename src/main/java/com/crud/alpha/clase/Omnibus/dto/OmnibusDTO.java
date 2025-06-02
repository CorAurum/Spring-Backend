package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.enums.EstadoOmnibus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OmnibusDTO {
    private int nroCoche;
    private int asientos;
    private String descripcion;
    private EstadoOmnibus estado;
    private boolean accesibilidad;
    private String registeredByFullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Experimentales
    private String ultimaLocalidadNombre;
    private long ultimaLocalidadId;
}
