package com.crud.alpha.clase.Usuarios;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDetailsDTO {
    private String clerkId;
    private String email;
    private String nombre;
    private String apellido;
    private boolean activo;
    private String tipo; // Determined by the controller.
    private LocalDateTime fechaNacimiento;
    private String registeredByFullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
