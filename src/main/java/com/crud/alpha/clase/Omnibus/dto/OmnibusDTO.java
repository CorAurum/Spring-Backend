package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.enums.EstadoOmnibus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    // Nuevo atributo para los números de asiento, puede ser nulo al inicio ya que primero se crea el bus y dsp se asigna los asientos
    private List<Integer> nroAsientos;
}
