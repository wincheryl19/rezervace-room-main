package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Kontroler pro autentizaci a registraci uživatelů.
 * Poskytuje metody pro zobrazení domovské stránky, přihlašování a registraci uživatelů.
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     * Konstruktor pro injektování UserService.
     *
     * @param userService Služba pro správu uživatelů.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Zobrazení domovské stránky při přístupu na URL "/".
     *
     * @return Vrací šablonu "index.html".
     */
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    /**
     * Zobrazení přihlašovací stránky na URL "/login".
     *
     * @return Vrací šablonu "login.html".
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Zobrazení registračního formuláře na URL "/register".
     * Přidává nový objekt `UserDto` do modelu pro šablonu.
     *
     * @param model Model pro přenos dat do šablony.
     * @return Vrací šablonu "register.html".
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    /**
     * Zpracování registračního formuláře.
     * Validuje vstupní data, kontroluje existenci uživatele a v případě úspěchu uloží nového uživatele.
     *
     * @param userDto Data nového uživatele získaná z formuláře.
     * @param result Výsledek validace formuláře.
     * @param model Model pro vrácení dat do formuláře při chybě.
     * @return Přesměrování na registrační stránku při chybě, jinak na "/register?success".
     */
    @SuppressWarnings("null")
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {

        // Automatické nastavení role, pokud není zadána
        if (userDto.getRole() == null || userDto.getRole().isEmpty()) {
            userDto.setRole("ROLE_USER");
        }

        // Kontrola, zda uživatel s daným e-mailem již existuje
        if (userService.findUserByEmail(userDto.getEmail()) != null) {
            result.rejectValue("email", null, "Účet s tímto e-mailem již existuje");
        }

        // Pokud validace selže, vrací zpět na registrační formulář s chybovou zprávou
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        // Uložení nového uživatele do databáze
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
}
