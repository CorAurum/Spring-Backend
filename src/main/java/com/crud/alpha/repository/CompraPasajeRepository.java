package com.crud.alpha.repository;

import com.crud.alpha.clase.Pasaje.CompraPasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraPasajeRepository extends JpaRepository<CompraPasaje, Long> {

}
