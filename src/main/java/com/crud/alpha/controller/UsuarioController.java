package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.dto.UsuarioUpdateDTO;
import com.crud.alpha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{clerkId}")
    public Optional<Usuario> obtenerUsuario(@PathVariable String clerkId) {
        return usuarioService.obtenerPorClerkID(clerkId);
    }

    @GetMapping("/buscar")
    public Optional<Usuario> obtenerUsuarioporEmail(@RequestParam String email) {
        return usuarioService.obtenerPorEmail(email);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @PatchMapping("/{clerkId}")
    public ResponseEntity<Usuario> updateUsuario(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO usuarioActualizado) {

        Usuario updatedUsuario = usuarioService.actualizarUsuario(clerkId, usuarioActualizado);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
}
