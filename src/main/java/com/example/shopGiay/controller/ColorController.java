package com.example.shopGiay.controller;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.model.Status;
import com.example.shopGiay.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String listColors(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Color> colors = colorService.findAll(PageRequest.of(page, 5));
        model.addAttribute("colors", colors);
        model.addAttribute("currentPage", page);
        return "colors/list";
    }

    @GetMapping("/add")
    public String showAddForm(Color color) {
        return "colors/add";
    }

    @PostMapping("/add")
    public String addColor(@Valid Color color, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "colors/add";
        }
        color.setCreateDate(LocalDate.now());
        colorService.save(color);
        return "redirect:/colors";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Color color = colorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid color Id:" + id));
        model.addAttribute("color", color);
        model.addAttribute("statuses", Status.values());
        return "colors/edit";
    }

    @PostMapping("/update/{id}")
    public String updateColor(@PathVariable("id") Integer id, @Valid Color color, BindingResult result, Model model) {
        if (result.hasErrors()) {
            color.setId(id);
            model.addAttribute("statuses", Status.values());
            return "colors/edit";
        }
        color.setUpdateDate(LocalDate.now());
        colorService.save(color);
        return "redirect:/colors";
    }

    @GetMapping("/delete/{id}")
    public String deleteColor(@PathVariable("id") Integer id, Model model) {
        Color color = colorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid color Id:" + id));
        colorService.deleteById(id);
        return "redirect:/colors";
    }
}
