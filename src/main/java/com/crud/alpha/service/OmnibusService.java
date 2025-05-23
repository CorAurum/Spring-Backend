package com.crud.alpha.service;

import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.OmnibusRepository;
import com.crud.alpha.repository.ViajeRepository;
import jakarta.transaction.Transactional;
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

    // eliminar bus por nroCoche
    @Transactional
    public void eliminarOmnibus(int nroCoche) {
        Optional<Omnibus> omnibus = omnibusRepository.findByNroCoche(nroCoche);
        if (omnibus.isPresent()) {
            omnibusRepository.deleteOmnibusByNroCoche(nroCoche);
        } else {
            throw new EntityNotFoundException("Omnibus con nroCoche " + nroCoche + " no encontrado.");
        }
    }




}