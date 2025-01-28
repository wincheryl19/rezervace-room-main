package com.example.repository;

import com.example.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>  {
    // Vyhledání knih podle kategorie
    List<Room> findByCategoryId(Long categoryId);
}