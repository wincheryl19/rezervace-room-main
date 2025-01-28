package com.example.controller;

import com.example.entity.Reservation;
import com.example.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  //  Změněno z @RestController na @Controller pro HTML stránku
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //  Vrací Thymeleaf šablonu `reservations.html`
    @GetMapping
    public String showReservationsPage(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations"; // Odpovídá názvu `reservations.html` v templates/
    }

    //  API endpoint pro získání rezervací ve formátu JSON (použití na /reservations/api)
    @GetMapping("/api")
    @ResponseBody
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    //  API pro získání jedné rezervace podle ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    //  API pro vytvoření rezervace
    @PostMapping("/api")
    @ResponseBody
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.saveReservation(reservation);
    }

    //  API pro aktualizaci rezervace
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        Reservation existingReservation = reservationService.getReservationById(id);
        if (existingReservation != null) {
            updatedReservation.setId(id);
            return ResponseEntity.ok(reservationService.saveReservation(updatedReservation));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //  API pro smazání rezervace
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
