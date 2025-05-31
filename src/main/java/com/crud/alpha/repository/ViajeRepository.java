package com.crud.alpha.repository;

import com.crud.alpha.clase.Viaje.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    List<Viaje> findByCerrado(boolean cerrado); //Metodo generado usando la funcion de JPA findBy

    Optional<Viaje> findById(Long id);

    // Hay que testear si estas funcionan, en vez de buscar por el atributo declarado,

    List<Viaje> findByLocalidadFinal_Id(Long id);

    List<Viaje> findByLocalidadInicial_Nombre(String nombre);

    //Deberia devolver una instancia que tenga la localida inicial y localidad final buscada por la Id de dicha localidad
    List<Viaje> findByLocalidadInicial_IdAndLocalidadFinal_Id(Long localidadInicialId, Long localidadFinalId);




}
