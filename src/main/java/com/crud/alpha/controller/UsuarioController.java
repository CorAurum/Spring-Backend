package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuario;
import com.crud.alpha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
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

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id); // Aseguramos que actualiza el usuario existente
        return usuarioService.guardarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
}
