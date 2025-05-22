package com.crud.alpha.clase.Viaje.dto;


import com.crud.alpha.clase.Viaje.Viaje;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViajeDTO {
    private Long id;
    private String fecha;
    private String horaPartida;
    private String horaLlegada;
    private boolean cerrado;
    private int nroCoche;
    private String localidadInicial;
    private String localidadFinal;

}
