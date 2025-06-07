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

    List<Viaje> findByLocalidadDestino_Id(Long id);

    List<Viaje> findByLocalidadOrigen_Nombre(String nombre);

    // Devolver una instancia de viaje que tenga la localida origen y localidad destino buscada por la Id de dicha localidad
    List<Viaje> findByLocalidadOrigen_IdAndLocalidadDestino_Id(Long localidadOrigenId, Long localidadDestinoId);




}
