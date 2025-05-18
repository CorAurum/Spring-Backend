package com.crud.alpha.clase.Usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendedor")
@Getter
@Setter
public class Vendedor extends Usuario {

    // Fecha de ingreso a la empresa
    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

}