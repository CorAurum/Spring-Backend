package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Administrador.Administrador;
import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import com.crud.alpha.clase.Usuarios.UserDetailsDTO;
import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    // ** PRIVATE
    @Autowired
    private UsuarioService usuarioService;

    private UserDetailsDTO convertToUserDetailsDTO(Usuario usuario) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setClerkId(usuario.getClerkId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setActivo(usuario.isActivo());
        // Determine user type using instanceof
        if (usuario instanceof Administrador) {
            dto.setTipo("ADMINISTRADOR");
        } else if (usuario instanceof Cliente) {
            dto.setTipo("CLIENTE");
        } else if (usuario instanceof Vendedor){
            dto.setTipo("VENDEDOR");
        } else {
            dto.setTipo("DESCONOCIDO"); // Fallback for unexpected types
        }

        return dto;
    }

    // Obtener los detalles de un usuario por su id de Clerk.
    @GetMapping("/{clerkId}")
    public ResponseEntity<UserDetailsDTO> findEntity(@PathVariable String clerkId) {
        try {
            Usuario entity = usuarioService.findEntity(clerkId);
            UserDetailsDTO dto = convertToUserDetailsDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
