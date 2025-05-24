package com.crud.alpha.service;

import com.crud.alpha.repository.VentaPasajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaPasajeService {

    @Autowired
    private VentaPasajeRepository VentaPasajeRepository;

    // Eliminar Pago
    public void eliminarPago(Long CompraPasajeId) {
        VentaPasajeRepository.deleteById(CompraPasajeId);
    }
}
