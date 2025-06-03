package com.crud.alpha.service;

import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Pasaje.dto.VentaPasajeDTO;
import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.exceptions.VentaPasajeUpdateException;
import com.crud.alpha.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaPasajeService {

    @Autowired
    private VentaPasajeRepository ventaPasajeRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private PasajeRepository pasajeRepository;
    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private ClienteService clienteService;

    @Transactional
    public VentaPasaje crearVentaPasaje(VentaPasajeDTO dto) {
        VentaPasaje venta = new VentaPasaje();

        if(dto.getPaymentId() == null) {
          venta.setPaymentId(null);
        }else{
            venta.setPaymentId(dto.getPaymentId());
        }

        venta.setFechaVenta(dto.getFechaVenta());
        venta.setPaymentStatus(dto.getPaymentStatus());

        System.out.println("Buyer ID: " + dto.getBuyerId());
        System.out.println("Seller ID: " + dto.getSellerId());

        if (dto.getSellerId() != null) {
            Vendedor vendedor = vendedorRepository.findByClerkId(dto.getSellerId())
                    .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
            venta.setSellerId(vendedor);
        }

        if (dto.getBuyerId() != null) {
            Cliente cliente = clienteRepository.findByClerkId(dto.getBuyerId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            venta.setBuyerId(cliente);
        }


        Viaje viaje = viajeRepository.findById(dto.getViajeId())
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        venta.setPrecioViaje(viaje);

        if (dto.getPasajesIds() != null && !dto.getPasajesIds().isEmpty()) {
            List<Pasaje> pasajes = pasajeRepository.findAllById(dto.getPasajesIds());
            venta.setIdPasaje(pasajes);
        }



        return ventaPasajeRepository.save(venta);
    }

    public List<VentaPasaje> listarVentas() {
        return ventaPasajeRepository.findAll();
    }

    public VentaPasaje obtenerVentaPorId(Long id) {
        return ventaPasajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }


    // Actualiza una VentaPasaje cuando el pago fue exitoso con los nuevos datos.
    @Transactional
    public VentaPasaje actualizar(VentaPasaje ventaPasaje) {
        try {
            if (ventaPasaje.getId() == null) {
                throw new VentaPasajeUpdateException("La venta no tiene un ID asignado.");
            }

            if (!ventaPasajeRepository.existsById(ventaPasaje.getId())) {
                throw new VentaPasajeUpdateException("No se encontró la venta con ID: " + ventaPasaje.getId());
            }

            return ventaPasajeRepository.save(ventaPasaje);

        } catch (Exception e) {
            throw new VentaPasajeUpdateException("Error al actualizar la venta: " + e.getMessage());
        }
    }

    //Actualiza los IdVentaPasaje en los correspondientes pasajes comprados, mediante el aviso del webhook
    public void asignarVentaAPasajes(List<Integer> pasajeIds, VentaPasaje ventaPasaje) {

        for (Integer pasajeId : pasajeIds) {
            Optional<Pasaje> optionalPasaje = pasajeRepository.findById(Long.valueOf(pasajeId));
            if (optionalPasaje.isPresent()) {
                Pasaje pasaje = optionalPasaje.get();
                pasaje.setVentaPasaje(ventaPasaje);
                pasajeRepository.save(pasaje);
            }


        }

    }
}

