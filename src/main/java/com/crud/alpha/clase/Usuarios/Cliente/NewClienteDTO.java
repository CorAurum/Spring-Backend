package com.crud.alpha.clase.Usuarios.Cliente;


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
    private Beneficiario tipoBeneficiario;
    private LocalDateTime fechaNacimiento;
    private String registeredBy;
}
