package com.crud.alpha.clase;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {}
