package com.crud.alpha.repository;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Omnibus.Omnibus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OmnibusRepository extends JpaRepository<Omnibus, Long> {

    Optional<Omnibus> findByNroCoche(int nroCoche); //Metodo generado usando la funcion de JPA findBy

    void deleteOmnibusByNroCoche(int nroCoche);

    boolean existsByNroCoche(int nroCoche);
}
