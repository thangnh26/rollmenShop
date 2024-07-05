package com.example.shopGiay.controller;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/brands/list";
    }

    @GetMapping("/new")
    public String createBrandForm(Model model) {
        Brand brand = new Brand();
        model.addAttribute("brand", brand);
        return "admin/brands/new";
    }

    @PostMapping
    public String saveBrand(@ModelAttribute("brand") Brand brand) {
        brand.setStatus(1);
        brand.setCreateDate(LocalDate.now());
        brandService.saveBrand(brand);
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String editBrandForm(@PathVariable Integer id, Model model) {
        Brand brand = brandService.getBrandById(id);
        model.addAttribute("brand", brand);
        return "admin/brands/edit";
    }

    @PostMapping("/{id}")
    public String updateBrand(@PathVariable Integer id, @ModelAttribute("brand") Brand brand, Model model) {
        Brand existingBrand = brandService.getBrandById(id);
        existingBrand.setId(id);
        existingBrand.setNameBrand(brand.getNameBrand());
        existingBrand.setDescription(brand.getDescription());
        existingBrand.setUpdateDate(LocalDate.now());
        existingBrand.setStatus(brand.getStatus());
        brandService.saveBrand(existingBrand);
        return "redirect:/brands";
    }

    @GetMapping("/{id}")
    public String deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return "redirect:/brands";
    }
}
