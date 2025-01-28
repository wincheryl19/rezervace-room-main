package com.example.service;

import com.example.entity.Room;
import com.example.entity.Category;
import com.example.repository.RoomRepository;
import com.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Získání všech místností
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Získání místnosti podle ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    // Uložení nebo aktualizace místnosti
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    // Smazání místnosti podle ID
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
