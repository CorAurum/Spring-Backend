package com.crud.alpha.clase.Usuarios.Vendedor;


import com.crud.alpha.enums.Beneficiario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VendedorUpdateDTO {
    private String nombre;
    private String apellido;
    private boolean activo;
    private LocalDateTime fechaNacimiento;
    private LocalDateTime fechaIngreso;

}
