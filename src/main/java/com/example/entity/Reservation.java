package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Uživatel, který rezervaci vytvořil

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // Rezervovaná místnost

    @Column(nullable = false)
    private LocalDate reservationDate; // Datum rezervace

    @Column(nullable = false)
    private LocalTime startTime; // Čas začátku rezervace

    @Column(nullable = false)
    private LocalTime endTime; // Čas konce rezervace

    @Column
    private String note; // Poznámka k rezervaci (volitelné)
}
