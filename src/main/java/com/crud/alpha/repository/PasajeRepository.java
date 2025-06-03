package com.crud.alpha.repository;

import com.crud.alpha.clase.Pasaje.Pasaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeRepository extends JpaRepository<Pasaje, Long> {
    List<Pasaje> findByViaje_id(Long id);
}
