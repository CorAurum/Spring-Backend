package com.crud.alpha.clase;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;




//Modelado de la clase y tabla usuarios, @Tabla le da el nombre a la tabla, los demas @ nos ahorran declarar los getters
//Setters y constructor vacio y con argumentos.
// el @Id establece que atributo va a ser la clave primaria y el Identity hace que cada nuevo ID autogenerado sea el siguiente del anterior.

@Entity
@Table(name = "CompraPasaje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraPasaje {
// Importante no nombrar atributos utilizando '_' (piso/barra baja)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PaymentId;

    @Column(nullable = false, unique = true)
    private String clerkId;

    @Column(nullable = false, unique = true)
    private String paymentStatus;


}




