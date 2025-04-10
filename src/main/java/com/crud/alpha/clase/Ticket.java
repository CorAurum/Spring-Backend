package com.crud.alpha.clase;

import com.crud.alpha.enums.Localidades;
import com.crud.alpha.enums.Viaje;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.util.List;

//Modelado de la clase y tabla de Ticket
@Entity
@Audited
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String departDate; // Formato "2025-04-23"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Localidades destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Localidades origin;

    @Column(nullable = false)
    private int passengers;

    @Column
    private String returnDate; // Formato "2025-04-23" o null

    @ElementCollection
    @CollectionTable(name = "selected_seat_numbers", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "seat_number")
    private List<Integer> selectedSeatNumbers;

    @ElementCollection
    @CollectionTable(name = "selected_seats", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "seat")
    private List<String> selectedSeats;

    @Column(nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Viaje tripType;
}
