package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService {

    @Autowired
    private LocalidadRepository LocalidadRepository;

    // Obtener todas las localidades
    public List<Localidad> ListarLocalidades() {
        return LocalidadRepository.findAll();
    }

    // Obtener Localidad por Nombre
    public Optional<Localidad> ObtenerLocalidadPorNombre(String nombreLocalidad) { return LocalidadRepository.findByNombre(nombreLocalidad);}


}