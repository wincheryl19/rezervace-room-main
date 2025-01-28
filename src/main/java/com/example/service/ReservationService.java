package com.example.service;

import com.example.entity.Reservation;
import com.example.entity.User;
import com.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // Získání všech rezervací
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Získání rezervace podle ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    // Uložení nebo aktualizace rezervace
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Smazání rezervace podle ID
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Získání rezervací pro konkrétní místnost
    public List<Reservation> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    // Získání rezervací pro konkrétního uživatele
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}