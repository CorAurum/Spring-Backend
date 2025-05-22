package com.crud.alpha.controller;
import com.crud.alpha.clase.Usuarios.Vendedor.NewVendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorUpdateDTO;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios/vendedores")
public class VendedorController {

    // ** PRIVATE
    @Autowired
    private VendedorService vendedorService;

    private VendedorDTO convertToDTO(Vendedor vendedor) {
        VendedorDTO dto = new VendedorDTO();
        dto.setClerkId(vendedor.getClerkId());
        dto.setEmail(vendedor.getEmail());
        dto.setNombre(vendedor.getNombre());
        dto.setApellido(vendedor.getApellido());
        dto.setActivo(vendedor.isActivo());
        // PONER LOS QUE FALTAN
        dto.setFechaNacimiento(vendedor.getFechaNacimiento());
        dto.setFechaIngreso(vendedor.getFechaIngreso());
        return dto;
    }

    // ** PUBLIC
    // Devolver una lista de todos los Vendedores del sistema.
    @GetMapping
    public ResponseEntity<List<VendedorDTO>> listEntities() {
        try {
            List<Vendedor> vendedores = vendedorService.listEntities();
            List<VendedorDTO> vendedoresDTO = vendedores.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(vendedoresDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Obtener un Vendedor por su id de Clerk.
    @GetMapping("/{clerkId}")
    public ResponseEntity<VendedorDTO> findEntity(@PathVariable String clerkId) {
        try {
            Vendedor entity = vendedorService.findEntity(clerkId);
            VendedorDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Obtener un Vendedor por su email.
    @GetMapping("/buscar")
    public ResponseEntity<VendedorDTO> findEntityByEmail(@RequestParam String email) {
        try {
            Vendedor entity = vendedorService.findEntityByEmail(email);
            VendedorDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear un nuevo Vendedor.
    @PostMapping
    public ResponseEntity<NewVendedorDTO> createEntity(@RequestBody NewVendedorDTO entityDTO) {
        try {
            NewVendedorDTO savedEntityDTO = vendedorService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntityDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Actualizar los datos de un Vendedor existente.
    @PatchMapping("/{clerkId}")
    public ResponseEntity<VendedorDTO> updateEntity(
            @PathVariable String clerkId,
            @RequestBody VendedorUpdateDTO updateDTO) {
        try {
            VendedorDTO updatedEntityDTO = vendedorService.updateEntity(clerkId, updateDTO);
            return ResponseEntity.ok(updatedEntityDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar un Vendedor por la id de Clerk.
    @DeleteMapping("/{clerkId}")
    public ResponseEntity<String> deleteEntity(@PathVariable String clerkId) {
        try {
            vendedorService.deleteEntity(clerkId);
            return ResponseEntity.ok("Vendedor eliminado con éxito");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar cliente");
        }
    }

}
