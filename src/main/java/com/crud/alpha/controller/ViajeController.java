package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.Viaje.dto.ViajeDTO;
import com.crud.alpha.service.VendedorService;
import com.crud.alpha.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private VendedorService vendedorService;

    // ✅ Convertir manualmente a DTO // Este metodo me lo dio Gepeto lo voy a testear
    private ViajeDTO convertirAViajeDTO(Viaje viaje) {
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setFecha(viaje.getFecha().toString());
        dto.setHoraPartida(viaje.getHora_partida().toString());
        dto.setHoraLlegada(viaje.getHora_llegada().toString());
        dto.setCerrado(viaje.isCerrado());
        dto.setNroCoche(viaje.getOmnibusAsignado().getNroCoche());
        dto.setLocalidadInicial(viaje.getLocalidadInicial().getNombre());
        dto.setLocalidadFinal(viaje.getLocalidadFinal().getNombre());
        dto.setCreatedAt(viaje.getCreatedAt());
        dto.setUpdatedAt(viaje.getUpdatedAt());
        dto.setRegisteredByFullName(viaje.getRegisteredBy().getNombre() + " " + viaje.getRegisteredBy().getApellido());
        return dto;
    }

    // Obtener todos los viajes
    @GetMapping
    public ResponseEntity<List<ViajeDTO>> listarViajes() {
        try {
            List<Viaje> viajes = viajeService.ListarViajes();
            List<ViajeDTO> viajeDTOs = viajes.stream()
                    .map(this::convertirAViajeDTO)
                    .toList();

            return ResponseEntity.ok(viajeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener viajes por nombre de localidad final
    @GetMapping("/{destino}")
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadDestino(@PathVariable String destino) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadFinal(destino);
            if (viajes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<ViajeDTO> viajeDTOs = viajes.stream()
                    .map(this::convertirAViajeDTO)
                    .toList();

            return ResponseEntity.ok(viajeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener viajes por localidad inicial y final
    @GetMapping("/localidades_{origen}_{destino}")
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadInicialYFinal(
            @PathVariable String origen,
            @PathVariable String destino) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadInicialyFinal(origen, destino);
            List<ViajeDTO> viajeDTOs = viajes.stream()
                    .map(this::convertirAViajeDTO)
                    .toList();

            return ResponseEntity.ok(viajeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //Crear un viaje
    @PostMapping
    public ResponseEntity<String> crearViaje(@RequestBody ViajeDTO dto) {
        try {
            Viaje viaje = new Viaje();

            Vendedor vendedor = vendedorService.findEntity(dto.getRegisteredByFullName());

            viaje.setFecha(LocalDate.parse(dto.getFecha()));
            viaje.setHora_partida(LocalTime.parse(dto.getHoraPartida()));
            viaje.setHora_llegada(LocalTime.parse(dto.getHoraLlegada()));
            viaje.setCerrado(dto.isCerrado());
            viaje.setCreatedAt(LocalDateTime.now());
            viaje.setUpdatedAt(LocalDateTime.now());
            viaje.setRegisteredBy(vendedor);

            viajeService.asignarDatosRelacionados(viaje, dto.getNroCoche(), dto.getLocalidadInicial(), dto.getLocalidadFinal());

            viajeService.guardarViaje(viaje);
            return ResponseEntity.ok("Viaje creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear viaje: " + e.getMessage());
        }
    }

    // modifica un viaje
    @PatchMapping("/{id}")
    public ResponseEntity<String> modificarViaje(@PathVariable Long id, @RequestBody ViajeDTO dto) {
        try {
            Viaje viaje = viajeService.buscarViajeporId(id)
                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

            // Solo se actualizan los campos editables
            viaje.setFecha(LocalDate.parse(dto.getFecha()));
            viaje.setHora_partida(LocalTime.parse(dto.getHoraPartida()));
            viaje.setHora_llegada(LocalTime.parse(dto.getHoraLlegada()));
            viaje.setCerrado(dto.isCerrado());
            viaje.setUpdatedAt(LocalDateTime.now());

            viajeService.asignarDatosRelacionados(viaje, dto.getNroCoche(), dto.getLocalidadInicial(), dto.getLocalidadFinal());

            viajeService.guardarViaje(viaje);
            return ResponseEntity.ok("Viaje modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar viaje: " + e.getMessage());
        }
    }



}
