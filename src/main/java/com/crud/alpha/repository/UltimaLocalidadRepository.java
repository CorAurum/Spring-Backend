package com.crud.alpha.repository;

import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UltimaLocalidadRepository extends JpaRepository<UltimaLocalidad, Long> {

    UltimaLocalidad save(UltimaLocalidad ultimaLocalidad);
}
