package com.crud.alpha.clase.Usuarios.Cliente;


import com.crud.alpha.enums.Beneficiario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteDTO {
    private String clerkId;
    private String email;
    private String nombre;
    private String apellido;
    private boolean activo;
    private Beneficiario tipoBenef;
    private LocalDateTime fechaNacimiento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
