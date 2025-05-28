package com.crud.alpha.service;

import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Pasaje.dto.VentaPasajeDTO;
import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        venta.setPaymentId(dto.getPaymentId());
        venta.setFechaCompra(dto.getFechaCompra());
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
}
