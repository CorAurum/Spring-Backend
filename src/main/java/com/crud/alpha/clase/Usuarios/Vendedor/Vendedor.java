package com.crud.alpha.clase.Usuarios.Vendedor;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.Viaje.Viaje;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendedor")
@Getter
@Setter
public class Vendedor extends Usuario {

    // Fecha de ingreso a la empresa
    @Column(nullable = false)
    private LocalDateTime fechaIngreso;


    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Seguramente hacemos otra funcion aparte en el service para updatearle/crearle los omnibus creados, registro viaje y registro localidad.
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // FK tabla y relacion con Omnibus
    @OneToMany(mappedBy = "registeredBy")
    // mappedby indica a la tabla que la columna de esta relacion no se genera en Omnibus, si no en UltimaLocalidad.
    private List<Omnibus> OmnibusRegistrados = new ArrayList<>();


    // FK tabla y relacion con Viaje
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Viaje> RegistroViaje = new ArrayList<>();


    // FK tabla y relacion con Localidad
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Localidad> RegistroLocalidad = new ArrayList<>();

}