package com.crud.alpha.repository;

import com.crud.alpha.clase.Ticket;
import com.crud.alpha.clase.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
