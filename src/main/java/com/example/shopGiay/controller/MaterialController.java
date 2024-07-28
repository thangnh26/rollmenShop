package com.example.shopGiay.controller;


import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    MaterialService materialService;
    @GetMapping
    public String listMaterial(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String keyword) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials with status 1 or 0
        Page<Material> materialPage;
        if (keyword != null && !keyword.isEmpty()) {
            materialPage = materialService.searchMaterialByName(keyword, pageable);
        } else {
            materialPage = materialService.getMaterialByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("materials", materialPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", materialPage.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view

        return "admin/material/list";
    }

    @GetMapping("/new")
    public String createMaterialForm(Model model) {
        Material material = new Material();
        model.addAttribute("material", material);
        return "admin/material/new";
    }

    @PostMapping("/add")
    public String addMaterial(@Valid @ModelAttribute("material") Material material, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/material/new"; // Trả về lại form nếu có lỗi validation
        }
        material.setStatus(1);
        material.setCreateDate(LocalDate.now());
        // Lưu category vào cơ sở dữ liệu nếu hợp lệ
        materialService.saveMaterial(material);
        return "redirect:/material";
    }

    @GetMapping("/edit/{id}")
    public String editMaterialForm(@PathVariable Integer id, Model model) {
        Material material = materialService.getMaterialById(id);
        model.addAttribute("material", material);
        return "admin/material/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMaterial(@PathVariable Integer id,
                                 @Valid @ModelAttribute("material") Material material,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "admin/material/edit";
        }

        // Lấy danh mục hiện tại từ cơ sở dữ liệu
        Material existingMaterial = materialService.getMaterialById(id);
        existingMaterial.setName(material.getName());
        existingMaterial.setCode(material.getCode());
        existingMaterial.setStatus(material.getStatus());
        existingMaterial.setUpdateDate(LocalDate.now());
        // Lưu lại danh mục đã chỉnh sửa vào cơ sở dữ liệu
        materialService.saveMaterial(existingMaterial);

        return "redirect:/material";
    }
    @GetMapping("/{id}")
    public String deleteMaterial(@PathVariable Integer id) {
        Material material = materialService.getMaterialById(id);
        material.setStatus(2); // Set status to 2 to mark as deleted
        materialService.saveMaterial(material);
        return "redirect:/material";
    }

}

