package com.example.service;

import com.example.entity.Category;
import com.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Získání všech kategorií
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Získání kategorie podle ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Přidání nebo aktualizace kategorie
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Smazání kategorie podle ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
