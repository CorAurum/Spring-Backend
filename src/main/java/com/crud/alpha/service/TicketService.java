package com.crud.alpha.service;

import com.crud.alpha.clase.Ticket;
import com.crud.alpha.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Obtener todos los tickets
    public List<Ticket> obtenerTodos() {
        return ticketRepository.findAll();
    }

    // Obtener ticket por su ID
    public Optional<Ticket> obtenerPorId(Long id) {
        return ticketRepository.findById(id);
    }

    // Guardar un nuevo ticket
    public Ticket guardarTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // Actualizar un ticket (aquí podrías añadir lógica adicional si es necesario)
    public Ticket actualizarTicket(Long id, Ticket updatedTicket) {
        // Buscar el ticket existente por ID
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);

        // Si no existe, lanzar un error (o devolver null, según tu preferencia)
        if (!ticketOpt.isPresent()) {
            throw new IllegalArgumentException("Ticket no encontrado");
        }

        Ticket ticket = ticketOpt.get();

        // Actualizar los valores necesarios
        if (updatedTicket.getDepartDate() != null) {
            ticket.setDepartDate(updatedTicket.getDepartDate());
        }
        if (updatedTicket.getDestination() != null) {
            ticket.setDestination(updatedTicket.getDestination());
        }
        if (updatedTicket.getOrigin() != null) {
            ticket.setOrigin(updatedTicket.getOrigin());
        }
        if (updatedTicket.getPassengers() > 0) {
            ticket.setPassengers(updatedTicket.getPassengers());
        }
        if (updatedTicket.getReturnDate() != null) {
            ticket.setReturnDate(updatedTicket.getReturnDate());
        }
        if (updatedTicket.getSelectedSeatNumbers() != null) {
            ticket.setSelectedSeatNumbers(updatedTicket.getSelectedSeatNumbers());
        }
        if (updatedTicket.getSelectedSeats() != null) {
            ticket.setSelectedSeats(updatedTicket.getSelectedSeats());
        }
        if (updatedTicket.getTotalPrice() >= 0) {
            ticket.setTotalPrice(updatedTicket.getTotalPrice());
        }
        if (updatedTicket.getTripType() != null) {
            ticket.setTripType(updatedTicket.getTripType());
        }

        // Guardar el ticket actualizado
        return ticketRepository.save(ticket);
    }

    // Eliminar un ticket
    public void eliminarTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
