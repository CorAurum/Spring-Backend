package com.crud.alpha.clase.Usuarios.Administrador;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdministradorUpdateDTO {
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;
}
