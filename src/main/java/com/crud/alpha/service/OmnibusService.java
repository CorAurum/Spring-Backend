package com.crud.alpha.service;

import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.OmnibusRepository;
import com.crud.alpha.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OmnibusService {

    @Autowired
    private OmnibusRepository omnibusRepository;

    public List<Omnibus> listarOmnibus(){ return omnibusRepository.findAll();}

    public Optional<Omnibus> buscarOmnibusPorNroCoche(int nroCoche){ return omnibusRepository.findByNroCoche(nroCoche);}

    public void guardarOmnibus(Omnibus omnibus) {
        omnibusRepository.save(omnibus);
    }

}