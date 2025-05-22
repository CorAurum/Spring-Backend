package com.crud.alpha.controller;

import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.Viaje.dto.ViajeDTO;
import com.crud.alpha.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

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
    @GetMapping("/localidad_Destino")
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadDestino(@RequestParam String localidadDestino) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadFinal(localidadDestino);
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
    @GetMapping("/localidades_Inicio_Destino")
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadInicialYFinal(
            @RequestParam String localidadInicial,
            @RequestParam String localidadFinal) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadInicialyFinal(localidadInicial, localidadFinal);
            List<ViajeDTO> viajeDTOs = viajes.stream()
                    .map(this::convertirAViajeDTO)
                    .toList();

            return ResponseEntity.ok(viajeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
