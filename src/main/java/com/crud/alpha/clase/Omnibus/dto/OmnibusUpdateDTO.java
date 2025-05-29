package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.enums.EstadoOmnibus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OmnibusUpdateDTO {
    private int nroCoche;
    private int asientos;
    private String descripcion;
    private EstadoOmnibus estado;
    private boolean accesibilidad;
    private String registeredBy;
    private long ultimaLocalidadId;
}
