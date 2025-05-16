package com.crud.alpha.repository;

import com.crud.alpha.clase.CompraPasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraPasajeRepository extends JpaRepository<CompraPasaje, Long> {

}
