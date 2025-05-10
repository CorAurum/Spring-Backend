package com.crud.alpha.controller;

import com.crud.alpha.clase.*;
import com.crud.alpha.dto.UsuarioUpdateDTO;
import com.crud.alpha.enums.Tipo_Usuario;
import com.crud.alpha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/{clerkId}")
    public ResponseEntity<Usuario> updateUsuario(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO usuarioActualizado) {

        Usuario updatedUsuario = usuarioService.actualizarUsuario(clerkId, usuarioActualizado.getNombre(), usuarioActualizado.getApellido());
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

    // 👇 New endpoints to create specific subclasses

    @PostMapping("/crear/administrador")
    public Usuario crearAdministrador(@RequestBody Usuario dto) {
        Administrador admin = new Administrador();
        admin.setClerkId(dto.getClerkId());
        admin.setEmail(dto.getEmail());
        admin.setNombre(dto.getNombre());
        admin.setApellido(dto.getApellido());
        return usuarioService.guardarUsuario(admin);
    }

    @PostMapping("/crear/vendedor")
    public Usuario crearVendedor(@RequestBody Usuario dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setClerkId(dto.getClerkId());
        vendedor.setEmail(dto.getEmail());
        vendedor.setNombre(dto.getNombre());
        vendedor.setApellido(dto.getApellido());
        return usuarioService.guardarUsuario(vendedor);
    }

    @PostMapping("/crear/cliente")
    public Usuario crearCliente(@RequestBody Usuario dto) {
        Cliente cliente = new Cliente();
        cliente.setClerkId(dto.getClerkId());
        cliente.setEmail(dto.getEmail());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        return usuarioService.guardarUsuario(cliente);
    }
    @PatchMapping("/modificar/administrador/{clerkId}")
    public ResponseEntity<Usuario> modificarAdministrador(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorClerkID(clerkId);

        if (usuarioOpt.isPresent() && usuarioOpt.get() instanceof Administrador) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            Usuario actualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/modificar/vendedor/{clerkId}")
    public ResponseEntity<Usuario> modificarVendedor(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorClerkID(clerkId);

        if (usuarioOpt.isPresent() && usuarioOpt.get() instanceof Vendedor) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            Usuario actualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/modificar/cliente/{clerkId}")
    public ResponseEntity<Usuario> modificarCliente(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorClerkID(clerkId);

        if (usuarioOpt.isPresent() && usuarioOpt.get() instanceof Cliente) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            Usuario actualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
