package com.example.controller;

import com.example.entity.Room;
import com.example.entity.Category;
import com.example.service.RoomService;
import com.example.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final CategoryService categoryService;

    public RoomController(RoomService roomService, CategoryService categoryService) {
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    //   Vrací HTML stránku `rooms.html`
    @GetMapping
    public String showRoomsPage(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        List<Category> categories = categoryService.getAllCategories();

        System.out.println("Načtené místnosti: " + rooms);
        System.out.println("Načtené kategorie: " + categories);

        model.addAttribute("rooms", rooms);
        model.addAttribute("categories", categories);

        return "rooms"; // Musí odpovídat názvu `rooms.html` v `templates/`
    }

    //   API: Vrací JSON se všemi místnostmi
    @GetMapping("/api")
    @ResponseBody
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    //   API: Získání konkrétní místnosti podle ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    //   API: Vytvoření nové místnosti
    @PostMapping("/api")
    @ResponseBody
    public Room createRoom(@RequestBody Room room) {
        return roomService.saveRoom(room);
    }

    //   API: Aktualizace místnosti
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        Room existingRoom = roomService.getRoomById(id);
        if (existingRoom != null) {
            updatedRoom.setId(id);
            return ResponseEntity.ok(roomService.saveRoom(updatedRoom));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //   API: Smazání místnosti
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
