package com.crud.alpha.clase;

import com.crud.alpha.enums.Tipo_Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;


//Modelado de la clase y tabla usuarios, @Tabla le da el nombre a la tabla, los demas @ nos ahorran declarar los getters
//Setters y constructor vacio y con argumentos.
// el @Id establece que atributo va a ser la clave primaria y el Identity hace que cada nuevo ID autogenerado sea el siguiente del anterior.

@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@Table(name = "usuarios")
@AuditTable("usuarios_AUD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String clerkId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


