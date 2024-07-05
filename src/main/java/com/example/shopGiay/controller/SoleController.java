package com.example.shopGiay.controller;


import com.example.shopGiay.model.Sole;
import com.example.shopGiay.service.SoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/soles")
public class SoleController {
    @Autowired
    private SoleService soleService;

    @GetMapping
    public String listSoles(Model model) {
        model.addAttribute("soles", soleService.getAllSoles());
        return "admin/soles/list";
    }

    @GetMapping("/new")
    public String createSoleForm(Model model) {
        Sole sole = new Sole();
        model.addAttribute("sole", sole);
        return "admin/soles/new";
    }

    @PostMapping
    public String saveSole(@ModelAttribute("sole") Sole sole) {
        sole.setStatus(1);
        sole.setCreateDate(LocalDate.now());
        soleService.saveSole(sole);
        return "redirect:/soles";
    }

    @GetMapping("/edit/{id}")
    public String editSoleForm(@PathVariable Integer id, Model model) {
        Sole sole = soleService.getSoleById(id);
        model.addAttribute("sole", sole);
        return "admin/soles/edit";
    }

    @PostMapping("/{id}")
    public String updateSole(@PathVariable Integer id, @ModelAttribute("sole") Sole sole, Model model) {
        Sole Soletc = soleService.getSoleById(id);
        Soletc.setId(id);
        Soletc.setName(sole.getName());
        Soletc.setUpdateDate(LocalDate.now());
        Soletc.setStatus(sole.getStatus());
        soleService.saveSole(Soletc);
        return "redirect:/soles";
    }

    @GetMapping("/{id}")
    public String deleteSole(@PathVariable Integer id) {
        soleService.deleteSoleById(id);
        return "redirect:/soles";
    }
}
