package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.ultimaLocalidad;
import com.crud.alpha.repository.UltimaLocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UltimaLocalidadService {

    @Autowired
    private UltimaLocalidadRepository ultimaLocalidadRepositoryrepository;

    public ultimaLocalidad guardar(ultimaLocalidad ultimaLocalidad) {
        return ultimaLocalidadRepositoryrepository.save(ultimaLocalidad);
    }
}
