package com.example.controller;

import com.example.entity.User;
import com.example.service.RoomService;
import com.example.service.CategoryService;
import com.example.service.ReservationService;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.entity.Role;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoomService RoomService;
    private final CategoryService categoryService;
    private final ReservationService ReservationService;
    private final UserService userService;

    public AdminController(RoomService RoomService, CategoryService categoryService, ReservationService ReservationService,
                           UserService userService) {
        this.RoomService = RoomService;
        this.categoryService = categoryService;
        this.ReservationService = ReservationService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> users = userService.findAllUsers().stream()
                .map(userDto -> {
                    User user = new User();
                    user.setId(userDto.getId());
                    user.setEmail(userDto.getEmail());
                    user.setName(userDto.getName()); // Nastavení jména

                    // Vyhledání objektu Role podle názvu
                    Role role = new Role();
                    role.setName(userDto.getRole()); // Nastavení názvu role (ROLE_ADMIN, ROLE_USER)
                    user.setRole(role); // Přiřazení role uživateli

                    return user;
                })
                .toList();

        model.addAttribute("users", users);
        return "admin/admin"; // Šablona pro admin stránku
    }

    @GetMapping("/Rooms")
    public String RoomsPage(Model model) {
        model.addAttribute("Rooms", RoomService.getAllRooms());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Rooms"; // Odkaz na Rooms.html
    }

    @GetMapping("/categories")
    public String categoriesPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories"; // Odkaz na categories.html
    }

    @GetMapping("/Reservations")
    public String ReservationsPage(Model model) {
        model.addAttribute("Reservations", ReservationService.getAllReservations());
        return "Reservations"; // Odkaz na Reservations.html
    }

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
