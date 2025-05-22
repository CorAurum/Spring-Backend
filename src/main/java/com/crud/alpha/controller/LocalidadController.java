package com.crud.alpha.controller;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Localidad.dto.LocalidadDTO;
import com.crud.alpha.service.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/localidades")
public class LocalidadController {

    @Autowired
    private LocalidadService localidadService;

    // Convertir entidad a DTO
    private LocalidadDTO convertirALocalidadDTO(Localidad localidad) {
        LocalidadDTO dto = new LocalidadDTO();
        dto.setNombre(localidad.getNombre());
        return dto;
    }

    // Obtener todas las localidades
    @GetMapping
    public ResponseEntity<List<LocalidadDTO>> listarLocalidades() {
        try {
            List<Localidad> localidades = localidadService.ListarLocalidades();
            List<LocalidadDTO> localidadDTOs = localidades.stream()
                    .map(this::convertirALocalidadDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(localidadDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Obtener una localidad por su nombre
    @GetMapping("/buscar")
    public ResponseEntity<LocalidadDTO> obtenerPorNombre(@RequestParam String nombre) {
        try {
            Optional<Localidad> localidadOpt = localidadService.ObtenerLocalidadPorNombre(nombre);
            if (localidadOpt.isPresent()) {
                return ResponseEntity.ok(convertirALocalidadDTO(localidadOpt.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
