package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.ViajeRepository;
import com.crud.alpha.repository.OmnibusRepository;
import com.crud.alpha.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;
    @Autowired
    public OmnibusRepository omnibusRepository;
    @Autowired
    public LocalidadRepository localidadRepository;


    // Obtener todos los Viajes.
    public List<Viaje> ListarViajes() {
        return viajeRepository.findAll();
    }

    // Obtener viaje por Id
    public Optional<Viaje> buscarViajeporId(long id) { return viajeRepository.findById(id);}

    // Obtener un Viaje por su LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadFinal(String nombreLocalidadDestino) { return viajeRepository.findByLocalidadFinal_Nombre(nombreLocalidadDestino);}

    // Obtener un Viaje por su LocalidadInicial y LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadInicialyFinal(String nombreLocalidadInicial, String nombreLocalidadFinal) { return viajeRepository.findByLocalidadInicial_NombreAndLocalidadFinal_Nombre(nombreLocalidadInicial, nombreLocalidadFinal);}

    // Guardar un  viaje
    public void guardarViaje(Viaje viaje) {




        viajeRepository.save(viaje);
    }

    public void asignarDatosRelacionados(Viaje viaje, int nroCoche, String localidadInicial, String localidadFinal) {
        Omnibus omnibus = omnibusRepository.findByNroCoche(nroCoche)
                .orElseThrow(() -> new RuntimeException("Ómnibus no encontrado"));

        Localidad origen = localidadRepository.findByNombre(localidadInicial)
                .orElseThrow(() -> new RuntimeException("Localidad origen no encontrada"));

        Localidad destino = localidadRepository.findByNombre(localidadFinal)
                .orElseThrow(() -> new RuntimeException("Localidad destino no encontrada"));

        viaje.setOmnibusAsignado(omnibus);
        viaje.setLocalidadInicial(origen);
        viaje.setLocalidadFinal(destino);
    }





}