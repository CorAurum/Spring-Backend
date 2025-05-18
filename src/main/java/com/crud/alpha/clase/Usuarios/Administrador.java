package com.crud.alpha.clase.Usuarios;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administrador")
@Getter
@Setter
public class Administrador extends Usuario {

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<RegistroUsuario> RegistroUsuario = new ArrayList<>();

}