package com.crud.alpha.repository;

import com.crud.alpha.clase.Viaje.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    Optional<Viaje> findByCerrado(boolean cerrado); //Metodo generado usando la funcion de JPA findBy

    // Hay que testear si estas funcionan, en vez de buscar por el atributo declarado,

    List<Viaje> findByLocalidadFinal_Nombre(String nombre);

    List<Viaje> findByLocalidadInicial_Nombre(String nombre);

    //Deberia devolver una instancia que tenga la localida inicial y localidad final buscada
    List<Viaje> findByLocalidadInicial_NombreAndLocalidadFinal_Nombre(String nombreLocalidadInicial, String nombreLocalidadFinal);

}
