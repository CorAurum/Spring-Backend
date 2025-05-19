package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Administrador;
import com.crud.alpha.clase.Usuarios.dto.AdministradorDTO;
import com.crud.alpha.clase.Usuarios.dto.AdministradorUpdateDTO;
import com.crud.alpha.clase.Usuarios.dto.NewAdministradorDTO;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.clase.Usuarios.exceptions.ServiceException;
import com.crud.alpha.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    // ** PRIVATE
    @Autowired
    private AdministradorService administradorService;

    private AdministradorDTO convertToDTO(Administrador admin) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.setClerkId(admin.getClerkId());
        dto.setEmail(admin.getEmail());
        dto.setNombre(admin.getNombre());
        dto.setApellido(admin.getApellido());
        dto.setActivo(admin.isActivo());
        dto.setFechaNacimiento(admin.getFechaNacimiento());
        return dto;
    }

    // Devolver una lista de todos los administradores del sistema.
    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> listEntities() {
        try {
            List<Administrador> administradores = administradorService.listEntities();
            List<AdministradorDTO> administradoresDTO = administradores.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(administradoresDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Obtener un administrador por su id de Clerk.
    @GetMapping("/{clerkId}")
    public ResponseEntity<AdministradorDTO> findEntity(@PathVariable String clerkId) {
        try {
            Administrador admin = administradorService.findEntity(clerkId);
            AdministradorDTO dto = convertToDTO(admin);
            return ResponseEntity.ok(dto);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Obtener un administrador por su email.
    @GetMapping("/buscar")
    public ResponseEntity<AdministradorDTO> findEntityByEmail(@RequestParam String email) {
        try {
            Administrador entity = administradorService.findEntityByEmail(email);
            AdministradorDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear un nuevo administrador.
    @PostMapping
    public ResponseEntity<NewAdministradorDTO> createEntity(@RequestBody NewAdministradorDTO adminDTO) {
        try {
            NewAdministradorDTO savedAdminDTO = administradorService.createEntity(adminDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdminDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Actualizar los datos de un administrador existente
    @PatchMapping("/{clerkId}")
    public ResponseEntity<AdministradorDTO> updateEntity(
            @PathVariable String clerkId,
            @RequestBody AdministradorUpdateDTO adminUpdateDTO) {
        try {
            AdministradorDTO updatedAdminDTO = administradorService.updateEntity(clerkId, adminUpdateDTO);
            return ResponseEntity.ok(updatedAdminDTO);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar un administrador por la id de Clerk.
    @DeleteMapping("/{clerkId}")
    public ResponseEntity<String> eliminarAdministradorPorClerkId(@PathVariable String clerkId) {
        try {
            administradorService.deleteEntity(clerkId);
            return ResponseEntity.ok("Administrador eliminado con éxito");
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar administrador");
        }
    }

}
