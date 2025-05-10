package com.crud.alpha.clase;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;

@Entity
@DiscriminatorValue("VENDEDOR")
@Audited
public class
Vendedor extends Usuario {}
