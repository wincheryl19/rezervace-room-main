package com.example.controller;

import com.example.entity.Category;
import com.example.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //   1. Zobrazení stránky `categories.html`
    @GetMapping
    public String showCategoriesPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new Category());
        return "categories";
    }

    //   2. Přidání nové kategorie
    @PostMapping
    public String addCategory(@ModelAttribute("newCategory") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories"; // Přesměrování zpět na stejnou stránku
    }

    //   3. Úprava existující kategorie
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category updatedCategory, RedirectAttributes redirectAttributes) {
        updatedCategory.setId(id);
        categoryService.saveCategory(updatedCategory);
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie byla úspěšně upravena!");
        return "redirect:/categories";
    }

    //   4. Smazání kategorie
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
