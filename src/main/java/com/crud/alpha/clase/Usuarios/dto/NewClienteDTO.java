package com.crud.alpha.clase.Usuarios.dto;


import com.crud.alpha.enums.Beneficiario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewClienteDTO {
    private String clerkId;
    private String email;
    private String nombre;
    private String apellido;
    private boolean activo;
    private Beneficiario tipoBenef;
    private LocalDateTime fechaNacimiento;
}
