package com.example.shopGiay.controller;

import com.example.shopGiay.model.Category;

import com.example.shopGiay.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @GetMapping
    public String listCagetory(Model model, @RequestParam(defaultValue = "0") int page) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials
        Page<Category> categoryPage = categoryService.getAllCategoryPaginated(pageable);

        // Add pagination information to the model
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());

        return "category/list";
    }

    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "category/new";
    }

    @PostMapping
    public String saveCategory(@ModelAttribute("brand") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/edit";
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Integer id, @ModelAttribute("category") Category category, Model model) {
        Category existingCategory = categoryService.getCategoryById(id);
        existingCategory.setId(id);
        existingCategory.setNameCategory(category.getNameCategory());
        existingCategory.setCreateDate(category.getCreateDate());
        existingCategory.setUpdateDate(category.getUpdateDate());
        existingCategory.setStatus(category.getStatus());
        categoryService.saveCategory(existingCategory);
        return "redirect:/category";
    }

    @GetMapping("/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        Category category =categoryService.getCategoryById(id);
        category.setStatus(0);
        categoryService.saveCategory(category);
        return "redirect:/category";
    }

}