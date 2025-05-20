package com.crud.alpha.clase.Usuarios.Administrador;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewAdministradorDTO {
    private String clerkId;
    private String email;
    private String nombre;
    private String apellido;
    private boolean activo;
    private LocalDateTime fechaNacimiento;
}
