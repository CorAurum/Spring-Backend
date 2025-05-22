package com.crud.alpha.controller;

import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Omnibus.dto.OmnibusDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.service.OmnibusService;
import com.crud.alpha.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/omnibus")
public class OmnibusController {

    @Autowired
    private OmnibusService omnibusService;

    @Autowired
    private VendedorService vendedorService;

    // Conversor a DTO
    private OmnibusDTO convertirAOmnibusDTO(Omnibus omnibus) {
        OmnibusDTO dto = new OmnibusDTO();
        dto.setActivo(omnibus.isActivo());
        dto.setCantAsientos(omnibus.getCantAsientos());
        dto.setDescripcion(omnibus.getDescripcion());
        dto.setNroCoche(omnibus.getNroCoche());
        dto.setEstado(omnibus.getEstado());
        dto.setAccesibilidad(omnibus.isAccesibilidad());
        return dto;
    }

    // Listar todos los ómnibus
    @GetMapping
    public ResponseEntity<List<OmnibusDTO>> listarOmnibus() {
        try {
            List<Omnibus> omnibuses = omnibusService.listarOmnibus();
            List<OmnibusDTO> omnibusDTOs = omnibuses.stream()
                    .map(this::convertirAOmnibusDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(omnibusDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Buscar un ómnibus por número de coche
    @GetMapping("/buscar")
    public ResponseEntity<OmnibusDTO> buscarPorNroCoche(@RequestParam int nroCoche) {
        try {
            Optional<Omnibus> omnibusOpt = omnibusService.buscarOmnibusPorNroCoche(nroCoche);
            if (omnibusOpt.isPresent()) {
                OmnibusDTO dto = convertirAOmnibusDTO(omnibusOpt.get());
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    // Crear Omnibus
    @PostMapping("/crear")
    public ResponseEntity<String> createOmnibus(@RequestBody OmnibusDTO dto) {
        try {
            // This will throw UsuarioNotFoundException if not found
            Vendedor vendedor = vendedorService.findEntity(dto.getClerkId());

            Omnibus omnibus = new Omnibus();
            omnibus.setActivo(dto.isActivo());
            omnibus.setCantAsientos(dto.getCantAsientos());
            omnibus.setDescripcion(dto.getDescripcion());
            omnibus.setNroCoche(dto.getNroCoche());
            omnibus.setEstado(dto.getEstado());
            omnibus.setAccesibilidad(dto.isAccesibilidad());
            omnibus.setVendedorId(vendedor); // Assuming it's a Vendedor entity here

            omnibusService.guardarOmnibus(omnibus);

            return ResponseEntity.ok("Ómnibus creado exitosamente.");
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body("No se encontró un vendedor con clerkId: " + dto.getClerkId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el ómnibus.");
        }
    }

}


