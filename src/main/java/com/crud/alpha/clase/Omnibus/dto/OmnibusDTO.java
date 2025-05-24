package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.clase.Localidad.ultimaLocalidad;
import com.crud.alpha.enums.EstadoOmnibus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OmnibusDTO {
    private String registeredByFullName;
    private String descripcion;
    private int nroCoche;
    private EstadoOmnibus estado;
    private boolean accesibilidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ultimaLocalidad> ultimasLocalidades;

    // Nuevo atributo para los números de asiento, puede ser nulo al inicio ya que primero se crea el bus y dsp se asigna los asientos
    private List<Integer> nroAsientos;
}
