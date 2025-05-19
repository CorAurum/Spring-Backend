package com.crud.alpha.clase.Usuarios.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UsuarioUpdateDTO {
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;
}
