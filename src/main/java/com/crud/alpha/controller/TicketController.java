package com.crud.alpha.controller;

import com.crud.alpha.clase.Ticket;
import com.crud.alpha.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Obtener todos los tickets
    @GetMapping
    public List<Ticket> obtenerTickets() {
        return ticketService.obtenerTodos();
    }

    // Obtener un ticket por su ID
    @GetMapping("/{id}")
    public Optional<Ticket> obtenerTicket(@PathVariable Long id) {
        return ticketService.obtenerPorId(id);
    }

    // Crear un nuevo ticket
    @PostMapping
    public Ticket crearTicket(@RequestBody Ticket ticket) {
        return ticketService.guardarTicket(ticket);
    }

    // Actualizar un ticket
    @PatchMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id,
            @RequestBody Ticket ticketActualizado) {

        // Actualiza el ticket con la nueva información
        Ticket updatedTicket = ticketService.actualizarTicket(id, ticketActualizado);
        return ResponseEntity.ok(updatedTicket);
    }

    // Eliminar un ticket
    @DeleteMapping("/{id}")
    public void eliminarTicket(@PathVariable Long id) {
        ticketService.eliminarTicket(id);
    }
}
