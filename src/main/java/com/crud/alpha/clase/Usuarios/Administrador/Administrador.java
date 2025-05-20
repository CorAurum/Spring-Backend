package com.crud.alpha.clase.Usuarios.Administrador;

import com.crud.alpha.clase.Usuarios.Usuario;
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
    private List<com.crud.alpha.clase.Usuarios.RegistroUsuario> RegistroUsuario = new ArrayList<>();


}