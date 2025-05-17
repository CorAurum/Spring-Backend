package com.crud.alpha.clase.Usuarios;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("VENDEDOR")
@Getter
@Setter
public class Vendedor extends Usuario {

}