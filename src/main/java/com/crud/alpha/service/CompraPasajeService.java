package com.crud.alpha.service;

import com.crud.alpha.repository.CompraPasajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraPasajeService {

    @Autowired
    private CompraPasajeRepository CompraPasajeRepository;

    // Eliminar Pago
    public void eliminarPago(Long CompraPasajeId) {
        CompraPasajeRepository.deleteById(CompraPasajeId);
    }
}
