package com.crud.alpha.repository;

import com.crud.alpha.clase.Localidad.ultimaLocalidad;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.service.UltimaLocalidadService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimaLocalidadRepository extends JpaRepository<ultimaLocalidad, Long> {

    ultimaLocalidad save(ultimaLocalidad ultimaLocalidad);
}
