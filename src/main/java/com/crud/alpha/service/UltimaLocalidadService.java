package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.repository.UltimaLocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UltimaLocalidadService {

    @Autowired
    private UltimaLocalidadRepository ultimaLocalidadRepositoryrepository;

    public UltimaLocalidad guardar(UltimaLocalidad ultimaLocalidad) {
        return ultimaLocalidadRepositoryrepository.save(ultimaLocalidad);
    }
}
