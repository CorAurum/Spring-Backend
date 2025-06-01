package com.crud.alpha.clase.Pasaje.dto;


import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Pasaje.VentaPasaje;
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
public class PasajeDTO {
    private Long id;
    private Asiento asiento;
    private VentaPasaje idVentaPasaje;
    private Viaje idViaje;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
