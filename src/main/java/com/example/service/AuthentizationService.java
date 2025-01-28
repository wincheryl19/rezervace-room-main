package com.example.service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Třída implementující UserDetailsService pro autentizaci uživatelů pomocí Spring Security.
 * Načítá uživatele z databáze na základě e-mailu a mapuje jejich roli na oprávnění (GrantedAuthority).
 */
@Service
public class AuthentizationService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Konstruktor pro injektování UserRepository.
     * @param userRepository Repozitář pro práci s uživateli.
     */
    public AuthentizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Načte uživatele na základě e-mailu pro autentizaci.
     * @param email E-mail uživatele.
     * @return UserDetails objekt obsahující informace o uživateli.
     * @throws UsernameNotFoundException Výjimka, pokud uživatel s daným e-mailem neexistuje.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Načtení uživatele z databáze pomocí Optional
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel s e-mailem " + email + " nebyl nalezen."));

        // Vytvoření UserDetails objektu z nalezeného uživatele
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRoleToAuthority(user.getRole())
        );
    }

    /**
     * Převede roli uživatele na kolekci GrantedAuthority (oprávnění).
     * @param role Role uživatele.
     * @return Kolekce oprávnění (GrantedAuthority).
     */
    private Collection<? extends GrantedAuthority> mapRoleToAuthority(Role role) {
        // Mapování role na SimpleGrantedAuthority pro autorizaci v Spring Security
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }
}
