package com.crud.alpha.clase.Omnibus.dto;


import com.crud.alpha.clase.Viaje.Viaje;
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

    @Column(nullable = true)
    private String ClerkId;
    private boolean activo;
    private int cantAsientos;
    private String descripcion;
    private int nroCoche;
    private EstadoOmnibus estado;
    private boolean accesibilidad;

}
