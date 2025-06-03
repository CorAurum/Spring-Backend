package com.crud.alpha.clase.Viaje.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewViajeDTO {
    private String fechaPartida;
    private String fechaLlegada;
    private Long localidadOrigenId; // localidad origen id
    private Long localidadDestinoId; // localidad destino id
    private boolean cerrado;
    private float precio;
    private int nroCoche; // omnibus asignado
    private String registeredBy; // seller's clerkId
}
