package com.example.controller;

import com.example.entity.Reservation;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.service.ReservationService;
import com.example.service.RoomService;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showReservationsPage(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("users", userService.findAllUsers()); // Pro výběr uživatele
        model.addAttribute("newReservation", new Reservation()); // Pro formulář
        return "reservations";
    }

    // ✅ Přidání rezervace
    @PostMapping
    public String addReservation(@ModelAttribute("newReservation") Reservation reservation) {
        reservationService.saveReservation(reservation);
        return "redirect:/reservations"; // Přesměrování zpět
    }

    // ✅ Úprava rezervace
    @PostMapping("/edit/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation updatedReservation) {
        updatedReservation.setId(id);
        reservationService.saveReservation(updatedReservation);
        return "redirect:/reservations";
    }

    // ✅ Smazání rezervace
    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}
