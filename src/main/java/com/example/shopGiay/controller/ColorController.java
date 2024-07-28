package com.example.shopGiay.controller;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.service.ColorService;
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
@RequestMapping("/colors")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping
    public String listColors(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Color> colors;

        if (keyword != null && !keyword.isEmpty()) {
            colors = colorService.searchColorsByName(keyword, pageable);
        } else {
            colors = colorService.getColorByStatusNot2(pageable);
        }

        model.addAttribute("colors", colors.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", colors.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/colors/list";
    }


    @GetMapping("/new")
    public String createColorForm(Model model) {
        Color color = new Color();
        model.addAttribute("color", color);
        return "admin/colors/new";
    }

    @PostMapping("/add")
    public String saveColor(@Valid @ModelAttribute("color") Color color, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/colors/new";
        }
        color.setStatus(1);
        color.setCreateDate(LocalDate.now());
        colorService.saveColor(color);
        return "redirect:/colors";
    }

    @GetMapping("/edit/{id}")
    public String editColorForm(@PathVariable Integer id, Model model) {
        Color color = colorService.getColorById(id);
        model.addAttribute("color", color);
        return "admin/colors/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateColor(@PathVariable Integer id, @Valid @ModelAttribute("color") Color color, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/colors/edit";
        }
        Color existingColor = colorService.getColorById(id);
        existingColor.setName(color.getName());
        existingColor.setCode(color.getCode());
        existingColor.setStatus(color.getStatus());
        existingColor.setUpdateDate(LocalDate.now());
        colorService.saveColor(existingColor);
        return "redirect:/colors";
    }

    @GetMapping("/{id}")
    public String deleteColor(@PathVariable Integer id) {
        colorService.deleteColorById(id);
        return "redirect:/colors";
    }
}
