package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.enums.EstadoOmnibus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OmnibusDTO {
    private String registeredBy;
    private String descripcion;
    private int nroCoche;
    private EstadoOmnibus estado;
    private boolean accesibilidad;
}
