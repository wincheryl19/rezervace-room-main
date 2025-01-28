package com.example.repository;

import com.example.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate; // Přidání správného importu
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Získání rezervací podle ID místnosti
    List<Reservation> findByRoomId(Long roomId);

    // Získání rezervací podle ID uživatele
    List<Reservation> findByUserId(Long userId);

    // Získání rezervací podle datumu
    List<Reservation> findByReservationDate(LocalDate date);
}