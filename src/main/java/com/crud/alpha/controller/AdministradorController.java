package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Administrador;
import com.crud.alpha.dto.UsuarioUpdateDTO;
import com.crud.alpha.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService AdministradorService;

    @GetMapping
    public List<Administrador> obtenerAdministradores() {
        return AdministradorService.obtenerTodos();
    }

    @GetMapping("/{clerkId}")
    public Optional<Administrador> obtenerAdministrador(@PathVariable String clerkId) {
        return AdministradorService.obtenerPorClerkID(clerkId);
    }

    @GetMapping("/buscar")
    public Optional<Administrador> obtenerAdministradoresoporEmail(@RequestParam String email) {
        return AdministradorService.obtenerPorEmail(email);
    }

    @PostMapping
    public Administrador crearAdministrador(@RequestBody Administrador Admin) {
        return AdministradorService.guardarAdministrador(Admin);
    }

    @PatchMapping("/{clerkId}")
    public ResponseEntity<Administrador> updateAdministrador(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO AdminActualizado) {

        Administrador updatedAdmin = AdministradorService.actualizarAdministrador(clerkId, AdminActualizado);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public void eliminarAdministrador(@PathVariable Long id) {
        AdministradorService.eliminarAdministrador(id);
    }
}
