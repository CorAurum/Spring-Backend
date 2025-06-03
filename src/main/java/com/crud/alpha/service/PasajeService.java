package com.crud.alpha.service;

import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.AsientoRepository;
import com.crud.alpha.repository.PasajeRepository;
import com.crud.alpha.repository.ViajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasajeService {

    @Autowired
    private PasajeRepository pasajeRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private AsientoRepository asientoRepository;


    // Crear pasajes para un viaje (uno por cada asiento del ómnibus asignado)
    public void crearPasajesParaViaje(Long idViaje) {
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con ID: " + idViaje));

        Omnibus omnibus = viaje.getOmnibusAsignado();

        // Buscar todos los asientos del ómnibus
        List<Asiento> asientos = asientoRepository.findByOmnibus_nroCoche(omnibus.getNroCoche());

        if (omnibus.getAsientos() == null || omnibus.getAsientos().isEmpty()) {
            throw new IllegalStateException("No se pueden crear pasajes: el ómnibus asignado no tiene asientos.");
        }

        for (Asiento asiento : asientos) {
            Pasaje pasaje = new Pasaje();
            pasaje.setAsiento(asiento);
            pasaje.setViaje(viaje);
            pasajeRepository.save(pasaje);
        }
    }

    public List<Pasaje> listarPasajes() {
        return pasajeRepository.findAll();
    }

    public List<Pasaje> listarPasajesPorViaje(Long idViaje) {
        return pasajeRepository.findByViaje_id(idViaje);
    }

    public Pasaje actualizarPasaje(Long id, Pasaje pasajeActualizado) {
        Pasaje existente = pasajeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pasaje no encontrado con ID: " + id));

        existente.setAsiento(pasajeActualizado.getAsiento());
        existente.setVentaPasaje(pasajeActualizado.getVentaPasaje());
        existente.setViaje(pasajeActualizado.getViaje());

        return pasajeRepository.save(existente);
    }

    public void eliminarPasaje(Long id) {
        if (!pasajeRepository.existsById(id)) {
            throw new EntityNotFoundException("Pasaje no encontrado con ID: " + id);
        }
        pasajeRepository.deleteById(id);
    }

    public Pasaje buscarPasajePorId(Long id) {
        return pasajeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pasaje no encontrado con ID: " + id));
    }
}
