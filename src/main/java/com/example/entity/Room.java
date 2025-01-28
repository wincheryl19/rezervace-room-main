package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Název místnosti (např. "Zasedací místnost A")

    @Column(nullable = false)
    private int capacity; // Kapacita místnosti

    @Column(nullable = true)
    private String equipment; // Vybavení místnosti (např. projektor, whiteboard)

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Typ místnosti (např. "Zasedací", "Konferenční")
}