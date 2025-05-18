package com.crud.alpha.clase.Usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class RegistroUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate fecha;


    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "id") // id seria el atributo PK de Usuario, idUsuario es el nombre que tendra en la BD este atributo.
    private Usuario idUsuario;


    @ManyToOne
    @JoinColumn(name = "idAdministrador", referencedColumnName = "id")  // id seria el atributo PK de Administrador, idAdministrador es el nombre que tendra en la BD este atributo.
    private Administrador idAdministrador;



}