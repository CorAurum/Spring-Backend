package com.crud.alpha.repository;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Viaje.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    Optional<Localidad> findByNombre(String nombre); //Metodo generado usando la funcion de JPA findBy



}
