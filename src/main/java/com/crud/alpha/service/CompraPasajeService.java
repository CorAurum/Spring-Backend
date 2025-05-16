package com.crud.alpha.service;

import com.crud.alpha.clase.CompraPasaje;
import com.crud.alpha.repository.CompraPasajeRepository;
import com.crud.alpha.repository.UsuarioRepository;
import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraPasajeService {

    @Autowired
    private CompraPasajeRepository CompraPasajeRepository;

    // Eliminar Pago
    public void eliminarPago(Long CompraPasajeId) {
        CompraPasajeRepository.deleteById(CompraPasajeId);
    }
}
