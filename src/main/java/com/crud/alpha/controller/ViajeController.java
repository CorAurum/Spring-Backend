package com.crud.alpha.controller;

import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.Viaje.dto.NewViajeDTO;
import com.crud.alpha.clase.Viaje.dto.ViajeDTO;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.repository.UltimaLocalidadRepository;
import com.crud.alpha.service.VendedorService;
import com.crud.alpha.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private UltimaLocalidadRepository ultimaLocalidadRepository;

    // Convertir a DTO
    private ViajeDTO convertirAViajeDTO(Viaje viaje) {
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setFechaPartida(viaje.getFechaPartida());
        dto.setFechaLlegada(viaje.getFechaLlegada());
        dto.setCerrado(viaje.isCerrado());
        dto.setNroCoche(viaje.getOmnibusAsignado().getNroCoche());
        dto.setLocalidadOrigenNombre(viaje.getLocalidadOrigen().getNombre());
        dto.setLocalidadDestinoNombre(viaje.getLocalidadDestino().getNombre());
        dto.setLocalidadOrigenId(viaje.getLocalidadOrigen().getId());
        dto.setLocalidadDestinoId(viaje.getLocalidadDestino().getId());
        dto.setRegisteredByFullName(viaje.getRegisteredBy().getNombre() + " " + viaje.getRegisteredBy().getApellido());
        dto.setCreatedAt(viaje.getCreatedAt());
        dto.setUpdatedAt(viaje.getUpdatedAt());
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
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadDestino(@PathVariable long destino) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadDestino(destino);
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
    // Le saque los _ en el path del request porque por alguna razon no andaba, con / anda precioso
    @GetMapping("/localidades/{origen}/{destino}")
    public ResponseEntity<List<ViajeDTO>> obtenerPorLocalidadInicialYFinal(
            @PathVariable Long origen,
            @PathVariable Long destino) {
        try {
            List<Viaje> viajes = viajeService.ObtenerPorLocalidadOrigenyDestino(origen, destino);
            List<ViajeDTO> viajeDTOs = viajes.stream()
                    .map(this::convertirAViajeDTO)
                    .toList();


            return ResponseEntity.ok(viajeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Crear un nuevo viaje.
    @PostMapping
    public ResponseEntity<String> createEntity(@RequestBody NewViajeDTO entityDTO) {
        try {
            viajeService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Viaje creado con exito");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("entidad-duplicada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    // modifica un viaje
//    @PatchMapping("/{id}")
//    public ResponseEntity<String> modificarViaje(@PathVariable Long id, @RequestBody ViajeDTO dto) {
//        try {
//            Viaje viaje = viajeService.buscarViajeporId(id)
//                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
//
//            // Solo se actualizan los campos editables
//            viaje.setFecha(LocalDate.parse(dto.getFecha()));
//            viaje.setHora_partida(LocalTime.parse(dto.getHoraPartida()));
//            viaje.setHora_llegada(LocalTime.parse(dto.getHoraLlegada()));
//            viaje.setCerrado(dto.isCerrado());
//            viaje.setUpdatedAt(LocalDateTime.now());
//
//            viajeService.asignarDatosRelacionados(viaje, dto.getNroCoche(), dto.getLocalidadInicial(), dto.getLocalidadFinal());
//
//            viajeService.guardarViaje(viaje);
//            return ResponseEntity.ok("Viaje modificado exitosamente.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar viaje: " + e.getMessage());
//        }
//    }

//    // Cerrar un viaje (cambia cerrado de false a true)
 // Cerrar un viaje (cambia cerrado de false a true)
    @PostMapping("/cerrar/{id}")
    public ResponseEntity<String> cerrarViaje(@PathVariable Long id) {
        try {
            Viaje viaje = viajeService.buscarViajeporId(id)
                    .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

            if (viaje.isCerrado()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El viaje ya está cerrado.");
            }

            viaje.setCerrado(true);
            viaje.setUpdatedAt(LocalDateTime.now());
            viajeRepository.save(viaje);

            // Creamos la nueva instancia de UltimaLocalidad
            UltimaLocalidad ultimaLocalidad = new UltimaLocalidad();
            ultimaLocalidad.setFecha(LocalDateTime.now());
            ultimaLocalidad.setOmnibus(viaje.getOmnibusAsignado());
            ultimaLocalidad.setLocalidad(viaje.getLocalidadDestino());

            ultimaLocalidadRepository.save(ultimaLocalidad);

            return ResponseEntity.ok("El viaje fue cerrado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cerrar el viaje: " + e.getMessage());
        }
    }


}
