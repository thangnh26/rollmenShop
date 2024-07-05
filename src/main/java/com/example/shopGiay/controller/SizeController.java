package com.example.shopGiay.controller;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Size;
import com.example.shopGiay.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/sizes")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public String listSizes(Model model) {
        model.addAttribute("sizes", sizeService.getAllSizes());
        return "admin/sizes/list";
    }
    @GetMapping("/new")
    public String createSizeForm(Model model) {
        Size size = new Size();
        model.addAttribute("size", size);
        return "admin/sizes/new";
    }
    @PostMapping
    public String saveSize(@ModelAttribute("size") Size size) {
        size.setStatus(1);
        size.setCreateDate(LocalDate.now());
        sizeService.saveSize(size);
        return "redirect:/sizes";
    }

    @GetMapping("/edit/{id}")
    public String editSizeForm(@PathVariable Integer id, Model model) {
        Size size = sizeService.getSizeById(id);
        model.addAttribute("size", size);
        return "admin/sizes/edit";
    }

    @PostMapping("/{id}")
    public String updateSize(@PathVariable Integer id, @ModelAttribute("size") Size size) {
        Size existingSize = sizeService.getSizeById(id);
        existingSize.setSizeNumber(size.getSizeNumber());
        existingSize.setUpdateDate(LocalDate.now());
        existingSize.setStatus(size.getStatus());
        sizeService.saveSize(existingSize);
        return "redirect:/sizes";
    }

    @GetMapping("/{id}")
    public String deleteSize(@PathVariable Integer id) {
        sizeService.deleteSizeById(id);
        return "redirect:/sizes";
    }
}
