package com.crud.alpha.controller;

import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Pasaje.dto.VentaPasajeDTO;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.service.VentaPasajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

            @RestController
            @RequestMapping("/api/public/venta")
            public class VentaPasajeController {

                @Autowired
                private VentaPasajeService ventaPasajeService;

                // POST: Crear nueva venta de pasaje
                @PostMapping
                public ResponseEntity<?> crearVentaPasaje(@RequestBody VentaPasajeDTO dto) {
                    try {
                        VentaPasaje venta = ventaPasajeService.crearVentaPasaje(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidad no encontrada: " + e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    // GET: Listar todas las ventas de pasajes
    @GetMapping
    public ResponseEntity<?> listarVentas() {
        try {
            List<VentaPasaje> ventas = ventaPasajeService.listarVentas();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener ventas: " + e.getMessage());
        }
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            VentaPasaje venta = ventaPasajeService.obtenerVentaPorId(id);
            return ResponseEntity.ok(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

