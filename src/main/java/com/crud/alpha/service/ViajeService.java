package com.crud.alpha.service;

import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository ViajeRepository;

    // Obtener todos los Viajes.
    public List<Viaje> ListarViajes() {
        return ViajeRepository.findAll();
    }

    // Obtener un Viaje por su LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadFinal(String nombreLocalidadDestino) { return ViajeRepository.findByLocalidadFinal_Nombre(nombreLocalidadDestino);}

    // Obtener un Viaje por su LocalidadInicial y LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadInicialyFinal(String nombreLocalidadInicial, String nombreLocalidadFinal) { return ViajeRepository.findByLocalidadInicial_NombreAndLocalidadFinal_Nombre(nombreLocalidadInicial, nombreLocalidadFinal);}

    //


}