package com.example.shopGiay.controller;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.service.BrandService;
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
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public String listBrands(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String keyword) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials with status 1 or 0
        Page<Brand> brands;
        if (keyword != null && !keyword.isEmpty()) {
            brands = brandService.searchBrandByName(keyword, pageable);
        } else {
            brands = brandService.getBrandByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("brands", brands.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view
        return "admin/brands/list";
    }

    @GetMapping("/new")
    public String createBrandForm(Model model) {
        Brand brand = new Brand();
        model.addAttribute("brand", brand);
        return "admin/brands/new";
    }

    @PostMapping
    public String saveBrand(@Valid @ModelAttribute("brand") Brand brand, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/brands/new"; // Trả về lại form nếu có lỗi validation
        }
        if (brandService.existsByNameBrand(brand.getNameBrand())) {
            // Nếu đã tồn tại, thêm thông báo lỗi và trả về lại form
            result.rejectValue("nameBrand", "error.brand", "Tên thương hiệu đã tồn tại!");
            return "admin/brands/new";
        }
        brand.setStatus(1);
        brand.setCreateDate(LocalDate.now());
        brandService.saveBrand(brand);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành công!");
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editBrandForm(@PathVariable Integer id, Model model) {
        Brand brand = brandService.getBrandById(id);
        model.addAttribute("brand", brand);
        return "admin/brands/edit";
    }

    @PostMapping("/{id}")
    public String updateBrand(@PathVariable Integer id,
                              @Valid @ModelAttribute("brand") Brand brand, BindingResult result,  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/brands/edit";
        }
        if (brandService.existsByNameBrandAndIdNot(brand.getNameBrand(), brand.getId())) {
            result.rejectValue("nameBrand", "error.brand", "Tên thương hiệu đã tồn tại!");
            return "admin/brands/edit";
        }
        Brand existingBrand = brandService.getBrandById(id);
        existingBrand.setId(id);
        existingBrand.setNameBrand(brand.getNameBrand());
        existingBrand.setDescription(brand.getDescription());
        existingBrand.setUpdateDate(LocalDate.now());
        existingBrand.setStatus(brand.getStatus());
        brandService.saveBrand(existingBrand);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa thành công!");
        return "redirect:/brands";
    }

    @GetMapping("/{id}")
    public String deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return "redirect:/brands";
    }
}
