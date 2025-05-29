package com.crud.alpha.repository;

import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Omnibus.Omnibus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    // Listar asientos filtrando por Omnibus (nroCoche)
    List<Asiento> findByOmnibus_nroCoche(int nroCoche);

    void deleteByOmnibus_nroCoche(int nroCoche);
}
