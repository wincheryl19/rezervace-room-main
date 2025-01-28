package com.example.controller;

import com.example.entity.Category;
import com.example.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ✅ Vrací HTML stránku categories.html a posílá do ní seznam kategorií
    @GetMapping
    public String showCategoriesPage(Model model) {
        List<Category> categories = categoryService.getAllCategories();

        System.out.println("Načtené kategorie: " + categories); // ✅ Logování

        model.addAttribute("categories", categories);

        return "categories"; // Musí odpovídat názvu souboru `categories.html`
    }

    // ✅ API: Vrací seznam kategorií v JSON formátu
    @GetMapping("/api")
    @ResponseBody
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // ✅ API: Získání konkrétní kategorie podle ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // ✅ API: Vytvoření nové kategorie
    @PostMapping("/api")
    @ResponseBody
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // ✅ API: Smazání kategorie
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
