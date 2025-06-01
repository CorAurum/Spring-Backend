package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Administrador.Administrador;
import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import com.crud.alpha.clase.Usuarios.UserDetailsDTO;
import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
// TODO: ENDPOINT PARA DESACTIVAR/ACTIVAR UN USUARIO

    // ** PRIVATE
    @Autowired
    private UsuarioService usuarioService;

    private UserDetailsDTO convertToDTO(Usuario usuario) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setClerkId(usuario.getClerkId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setActivo(usuario.isActivo());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setCreatedAt(usuario.getCreatedAt());
        dto.setUpdatedAt(usuario.getUpdatedAt());
        if (usuario.getRegisteredBy() != null) {
            dto.setRegisteredByFullName(usuario.getRegisteredBy().getNombre() + " " + usuario.getRegisteredBy().getApellido());
        } else {
            dto.setRegisteredByFullName("Sistema");
        }

        // Determine user type using instanceof
        if (usuario instanceof Administrador) {
            dto.setTipo("Administrador");
        } else if (usuario instanceof Cliente) {
            dto.setTipo("Cliente");
        } else if (usuario instanceof Vendedor) {
            dto.setTipo("Vendedor");
        } else {
            dto.setTipo("Desconocido"); // Fallback for unexpected types
        }

        return dto;
    }

    // Obtener todos los usuarios del sistema.
    @GetMapping
    public ResponseEntity<List<UserDetailsDTO>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listEntities();
            List<UserDetailsDTO> localidadDTOs = usuarios.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(localidadDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Obtener los detalles de un usuario por su id de Clerk.
    @GetMapping("/{clerkId}")
    public ResponseEntity<UserDetailsDTO> findEntity(@PathVariable String clerkId) {
        try {
            Usuario entity = usuarioService.findEntity(clerkId);
            UserDetailsDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Desactivar un usuario por su id de Clerk.
    @PatchMapping("/desactivar/{clerkId}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable String clerkId) {
        try {
            usuarioService.desactivarUsuario(clerkId);
            return ResponseEntity.ok("Usuario desactivado");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Activar un usuario por su id de Clerk.
    @PatchMapping("/activar/{clerkId}")
    public ResponseEntity<String> activarUsuario(@PathVariable String clerkId) {
        try {
            usuarioService.activarUsuario(clerkId);
            return ResponseEntity.ok("Usuario activado");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
