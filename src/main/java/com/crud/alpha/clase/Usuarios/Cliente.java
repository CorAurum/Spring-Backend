package com.crud.alpha.clase.Usuarios;

import com.crud.alpha.enums.Beneficiario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente extends Usuario {

    @Enumerated(EnumType.STRING)
    private Beneficiario tipoBenef;
}