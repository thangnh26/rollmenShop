package com.example.shopGiay.controller;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategorys(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) String keyword) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of categories with status 1 or 0
        Page<Category> categories;
        if (keyword != null && !keyword.isEmpty()) {
            categories = categoryService.searchCategoriesByName(keyword, pageable);
        } else {
            categories = categoryService.getCategoriesByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("categories", categories.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view

        return "admin/category/list";
    }


    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/category/new";
    }

    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/category/new"; // Trả về lại form nếu có lỗi validation
        }
        // Kiểm tra xem tên danh mục đã tồn tại chưa
        if (categoryService.existsByNameCategory(category.getNameCategory())) {
            // Nếu đã tồn tại, thêm thông báo lỗi và trả về lại form
            result.rejectValue("nameCategory", "error.category", "Tên thể loại đã tồn tại!");
            return "admin/category/new";
        }
        category.setStatus(1);
        category.setCreateDate(LocalDate.now());
        // Lưu category vào cơ sở dữ liệu nếu hợp lệ
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành công!");

        return "redirect:/category";
    }



    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Integer id,
                                 @Valid @ModelAttribute("category") Category category,
                                 BindingResult result, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem có lỗi validation không
        if (result.hasErrors()) {
            // Nếu có lỗi, trả về lại form chỉnh sửa với các thông tin lỗi
            return "admin/category/edit";
        }
        if (categoryService.existsByNameCategoryAndIdNot(category.getNameCategory(), category.getId())) {
            result.rejectValue("nameCategory", "error.category", "Tên thể loại đã tồn tại!");
            return "admin/category/edit";
        }
        // Lấy danh mục hiện tại từ cơ sở dữ liệu
        Category existingCategory = categoryService.getCategoryById(id);
        existingCategory.setNameCategory(category.getNameCategory());
        existingCategory.setStatus(category.getStatus());
        existingCategory.setUpdateDate(LocalDate.now());

        // Lưu lại danh mục đã chỉnh sửa vào cơ sở dữ liệu
        categoryService.saveCategory(existingCategory);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa thành công!");

        return "redirect:/category";
    }


    @GetMapping("/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        category.setStatus(2); // Set status to 2 to mark as deleted
        categoryService.saveCategory(category);
        return "redirect:/category";
    }
}
