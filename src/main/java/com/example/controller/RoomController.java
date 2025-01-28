package com.example.controller;

import com.example.entity.Room;
import com.example.entity.Category;
import com.example.service.RoomService;
import com.example.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final CategoryService categoryService;

    public RoomController(RoomService roomService, CategoryService categoryService) {
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showRoomsPage(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("room", new Room()); // Nový objekt pro formulář
        return "rooms";
    }

    //   Formulář pro úpravu místnosti
    @GetMapping("/edit/{id}")
    public String editRoom(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        if (room == null) {
            return "redirect:/rooms"; // Pokud místnost neexistuje, přesměrovat zpět
        }
        model.addAttribute("room", room);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-room"; // Odpovídá souboru `edit-room.html`
    }

    //   Uložení upravené místnosti
    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id, @ModelAttribute Room updatedRoom) {
        updatedRoom.setId(id);
        roomService.saveRoom(updatedRoom);
        return "redirect:/rooms"; // Přesměrování zpět na seznam místností
    }

    //   Uložení nové místnosti (data z formuláře na `rooms.html`)
    @PostMapping("/add")
    public String addRoom(@ModelAttribute Room room) {
        roomService.saveRoom(room);
        return "redirect:/rooms"; // Přesměrování zpět na seznam místností
    }

    //   Odstranění místnosti (přímé smazání na `rooms.html`)
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms"; // Přesměrování po smazání
    }
}

