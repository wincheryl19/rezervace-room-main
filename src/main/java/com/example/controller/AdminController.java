package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> users = userService.findAllUsers().stream()
                .map(userDto -> {
                    User user = new User();
                    user.setId(userDto.getId());
                    user.setEmail(userDto.getEmail());
                    user.setName(userDto.getName());
                    Role role = new Role();
                    role.setName(userDto.getRole());
                    user.setRole(role);
                    return user;
                })
                .toList();

        model.addAttribute("users", users);
        return "admin/admin";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            return "redirect:/admin"; // Pokud uživatel neexistuje, přesměruj zpět
        }

        List<Role> roles = userService.getAllRoles(); // Načteme všechny role

        model.addAttribute("user", user);
        model.addAttribute("roles", roles); // Přidáme role do modelu
        return "admin/edit-user";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("errorUser", "Uživatel nebyl nalezen.");
            return "redirect:/admin"; // Přesměrování zpět na admin, pokud uživatel neexistuje
        }

        // Správná konverze role (aby odpovídala objektu Role)
        Role role = userService.findRoleByName(updatedUser.getRole().getName());
        if (role == null) {
            redirectAttributes.addFlashAttribute("errorUser", "Role nebyla nalezena.");
            return "redirect:/admin";
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(role); // Nastavíme správný objekt Role

        userService.updateUser(id, existingUser);
        redirectAttributes.addFlashAttribute("successUser", "Uživatel byl úspěšně aktualizován.");

        return "redirect:/admin"; // Přesměrování zpět na admin panel po úspěšné změně
    }

    //     Smazání uživatele
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successUser", "Uživatel byl úspěšně odstraněn.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorUser", "Došlo k chybě při mazání uživatele.");
        }
        return "redirect:/admin";
    }
}
