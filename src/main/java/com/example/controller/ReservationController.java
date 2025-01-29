package com.example.controller;

import com.example.entity.Reservation;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.service.ReservationService;
import com.example.service.RoomService;
import com.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;  // ✅ Přidáno pro opravu chyby

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, RoomService roomService, UserService userService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.userService = userService;
    }

    // ✅ Zobrazení stránky s rezervacemi
    @GetMapping
    public String showReservationsPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        User loggedInUser = userService.findUserByEmail(email);

        boolean isAdmin = loggedInUser.getRole().getName().equals("ROLE_ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("rooms", roomService.getAllRooms());

        if (isAdmin) {
            model.addAttribute("users", userService.findAllUsers());
        } else {
            model.addAttribute("users", List.of(loggedInUser));
        }

        model.addAttribute("newReservation", new Reservation());
        return "reservations";
    }

    // ✅ Přidání rezervace
    @PostMapping
    public String addReservation(@ModelAttribute("newReservation") Reservation reservation, Authentication authentication) {
        String email = authentication.getName();
        User loggedInUser = userService.findUserByEmail(email);

        // Pokud není admin, nastavíme ho jako vlastníka rezervace
        if (!loggedInUser.getRole().getName().equals("ROLE_ADMIN")) {
            reservation.setUser(loggedInUser);
        }

        reservationService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    // ✅ Smazání rezervace
    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}
