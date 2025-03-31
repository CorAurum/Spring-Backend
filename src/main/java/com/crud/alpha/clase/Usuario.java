package com.crud.alpha.clase;

import com.crud.alpha.enums.Tipo_Usuario;
import jakarta.persistence.*;
import lombok.*;


//Modelado de la clase y tabla usuarios, @Tabla le da el nombre a la tabla, los demas @ nos ahorran declarar los getters
//Setters y constructor vacio y con argumentos.
// el @Id establece que atributo va a ser la clave primaria y el Identity hace que cada nuevo ID autogenerado sea el siguiente del anterior.

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String clerk_user_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo_Usuario tipo;
}