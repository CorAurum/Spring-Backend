package com.crud.alpha.clase.Usuarios.Cliente;


import com.crud.alpha.enums.Beneficiario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteUpdateDTO {
    private String nombre;
    private String apellido;
    private Beneficiario tipoBenef;
    private boolean activo;
    private LocalDateTime fechaNacimiento;
}
