package com.crud.alpha.repository;

import com.crud.alpha.clase.Pasaje.VentaPasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaPasajeRepository extends JpaRepository<VentaPasaje, Long> {

}
