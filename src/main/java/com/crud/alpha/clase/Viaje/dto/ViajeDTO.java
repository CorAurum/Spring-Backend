package com.crud.alpha.clase.Viaje.dto;


import com.crud.alpha.clase.Viaje.Viaje;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViajeDTO {
    private Long id;
    private LocalDateTime fechaPartida;
    private LocalDateTime fechaLlegada;
    private boolean cerrado;
    private int nroCoche;
    private Long localidadOrigenId;
    private Long localidadDestinoId;
    private String localidadOrigenNombre;
    private String localidadDestinoNombre;
    private String registeredByFullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
