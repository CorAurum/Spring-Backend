package com.crud.alpha.clase.Usuarios;

import com.crud.alpha.enums.Beneficiario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
public class Cliente extends Usuario {

    @Enumerated(EnumType.STRING)
    private Beneficiario tipoBenef;
}