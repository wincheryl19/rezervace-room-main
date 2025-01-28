package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.entity.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty(message = "Nickname nesmí být prázdné")
    private String name;

    @NotEmpty(message = "Email nesmí být prázdný")
    @Email(message = "Neplatný formát e-mailu")
    private String email;

    private String password; // Může být volitelný v některých případech (např. při úpravách uživatele)

    @NotEmpty(message = "Role nesmí být prázdná")
    private String role; // Používáme String pro název role
}
