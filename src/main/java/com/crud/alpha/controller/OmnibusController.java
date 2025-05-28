package com.crud.alpha.controller;

import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.clase.Localidad.dto.LocalidadDTO;
import com.crud.alpha.clase.Localidad.dto.NewLocalidadDTO;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Omnibus.dto.NewOmnibusDTO;
import com.crud.alpha.clase.Omnibus.dto.OmnibusDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.service.OmnibusService;
import com.crud.alpha.service.UltimaLocalidadService;
import com.crud.alpha.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/omnibus")
public class OmnibusController {

    @Autowired
    private OmnibusService omnibusService;

    private UltimaLocalidadService ultimaLocalidadService;

    @Autowired
    private VendedorService vendedorService;

    private OmnibusDTO convertToDTO(Omnibus omnibus) {
        OmnibusDTO dto = new OmnibusDTO();
        dto.setDescripcion(omnibus.getDescripcion());
        dto.setNroCoche(omnibus.getNroCoche());
        dto.setEstado(omnibus.getEstado());
        dto.setAccesibilidad(omnibus.isAccesibilidad());

        if (omnibus.getAsientos() != null && !omnibus.getAsientos().isEmpty()) {
            dto.setAsientos(omnibus.getAsientos().size());
        } else {
            dto.setAsientos(0);
        }

        dto.setRegisteredByFullName(omnibus.getRegisteredBy().getNombre() + " " + omnibus.getRegisteredBy().getApellido());
        dto.setCreatedAt(omnibus.getCreatedAt());
        dto.setUpdatedAt(omnibus.getUpdatedAt());

        return dto;
    }


    // Listar todos los ómnibus
    @GetMapping
    public ResponseEntity<List<OmnibusDTO>> listarOmnibus() {
        try {
            List<Omnibus> omnibuses = omnibusService.listEntities();
            List<OmnibusDTO> omnibusDTOs = omnibuses.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(omnibusDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Buscar un ómnibus por número de coche
    @GetMapping("/{nroCoche}")
    public ResponseEntity<OmnibusDTO> findEntity(@PathVariable int nroCoche) {
        try {
            Omnibus entity = omnibusService.findEntity(nroCoche);
            OmnibusDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear Omnibus
    @PostMapping
    public ResponseEntity<String> createEntity(@RequestBody NewOmnibusDTO entityDTO) {
        try {
           omnibusService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ómnibus creado con éxito");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("omnibus-duplicado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    //Modificar un omnibus por nroCoche
//    @PatchMapping("/{nroCoche}")
//    public ResponseEntity<String> modificarOmnibus(@PathVariable int nroCoche, @RequestBody OmnibusDTO dto) {
//        try {
//            Optional<Omnibus> omnibusOpt = omnibusService.findEntity(nroCoche);
//
//            if (omnibusOpt.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No se encontró un ómnibus con nroCoche: " + nroCoche);
//            }
//
//            Omnibus omnibus = omnibusOpt.get();
//
//            omnibus.setDescripcion(dto.getDescripcion());
//            omnibus.setEstado(dto.getEstado());
//            omnibus.setAccesibilidad(dto.isAccesibilidad());
//
//            // Actualizar updatedAt
//            omnibus.setUpdatedAt(LocalDateTime.now());
//
//            omnibusService.guardarOmnibus(omnibus);
//
//            return ResponseEntity.ok("Ómnibus modificado exitosamente.");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error al modificar el ómnibus: " + e.getMessage());
//        }
//    }



    // Eliminar un omnibus por nroCoche.
    @DeleteMapping("/{nroCoche}")
    public ResponseEntity<String> borrarOmnibus(@PathVariable int nroCoche) {
        try {
            omnibusService.eliminarOmnibus(nroCoche);
            return ResponseEntity.ok("Omnibus eliminado con éxito");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Omnibus: " + e.getMessage());
        }
    }


}


