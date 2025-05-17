package com.crud.alpha.controller;

import com.crud.alpha.clase.Usuarios.Cliente;
import com.crud.alpha.dto.UsuarioUpdateDTO;
import com.crud.alpha.enums.Beneficiario;
import com.crud.alpha.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerTodos();
    }

    @GetMapping("/{clerkId}")
    public Optional<Cliente> obtenerCliente(@PathVariable String clerkId) {
        return clienteService.obtenerPorClerkID(clerkId);
    }

    @GetMapping("/buscar")
    public Optional<Cliente> obtenerClientePorEmail(@RequestParam String email) {
        return clienteService.obtenerPorEmail(email);
    }

    @PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }

    @PatchMapping("/{clerkId}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable String clerkId,
            @RequestBody UsuarioUpdateDTO clienteActualizado) {

        Cliente clienteActualizadoResultado = clienteService.actualizarCliente(clerkId, clienteActualizado);
        return ResponseEntity.ok(clienteActualizadoResultado);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    @GetMapping("/tipo/{tipoBenef}")
    public List<Cliente> obtenerClientePorTipo(@PathVariable Beneficiario tipoBenef) {
        return clienteService.obtenerPorTipo(tipoBenef);
    }
}
