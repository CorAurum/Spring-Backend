package com.crud.alpha.clase.Usuarios;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO {
    private String clerkId;
    private String email;
    private String nombre;
    private String apellido;
    private boolean activo;
    private String tipo; // Determined by the controller.
}
