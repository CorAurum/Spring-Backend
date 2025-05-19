package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Administrador;
import com.crud.alpha.clase.Usuarios.dto.AdministradorDTO;
import com.crud.alpha.clase.Usuarios.dto.NewAdministradorDTO;
import com.crud.alpha.clase.Usuarios.dto.UsuarioUpdateDTO;
import com.crud.alpha.clase.Usuarios.exceptions.AdministradorNotFoundException;
import com.crud.alpha.clase.Usuarios.exceptions.AdministradorServiceException;
import com.crud.alpha.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    // Devolver una lista de todos los administradores del sistema.
    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> obtenerAdministradores() {
        try {
            List<Administrador> administradores = administradorService.obtenerTodos();
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
    public ResponseEntity<AdministradorDTO> obtenerAdministrador(@PathVariable String clerkId) {
        try {
            Administrador admin = administradorService.obtenerPorClerkID(clerkId);
            AdministradorDTO dto = convertToDTO(admin);
            return ResponseEntity.ok(dto);
        } catch (AdministradorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Obtener un administrador por su email.
    @GetMapping("/buscar")
    public Optional<Administrador> obtenerAdministradorPorEmail(@RequestParam String email) {
        return administradorService.obtenerPorEmail(email);
    }

    // Crear un nuevo administrador.
    @PostMapping
    public ResponseEntity<NewAdministradorDTO> crearAdministrador(@RequestBody NewAdministradorDTO adminDTO) {
        try {
            NewAdministradorDTO savedAdminDTO = administradorService.guardarAdministrador(adminDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdminDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (AdministradorServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Actualizar los datos de un administrador existente
    @PatchMapping("/{clerkId}")
    public ResponseEntity<Administrador> actualizarAdministrador(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO AdminActualizado) {

        Administrador updatedAdmin = administradorService.actualizarAdministrador(clerkId, AdminActualizado);
        return ResponseEntity.ok(updatedAdmin);
    }

    // Eliminar un administrador por la id de Clerk.
    @DeleteMapping("/{clerkId}")
    public ResponseEntity<String> eliminarAdministradorPorClerkId(@PathVariable String clerkId) {
        try {
            administradorService.eliminarAdministradorPorClerkId(clerkId);
            return ResponseEntity.ok("Administrador eliminado con éxito");
        } catch (AdministradorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AdministradorServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar administrador");
        }
    }

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
}
