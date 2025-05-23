package com.crud.alpha.clase.Localidad.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewLocalidadDTO {
    private String nombre;
    private String registeredBy;
    private String descripcion;
}
