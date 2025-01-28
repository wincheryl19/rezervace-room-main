package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entita reprezentující roli uživatele v aplikaci.
 * Každý uživatel má přiřazenou jednu roli, která definuje jeho oprávnění.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Název role (např. ROLE_USER, ROLE_ADMIN).
     * Musí být unikátní, což zajišťuje, že v databázi nebude duplicitní záznam.
     */
    @Column(nullable = false, unique = true)
    private String name;
}
