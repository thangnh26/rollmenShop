package com.example.shopGiay.controller;

import com.example.shopGiay.model.Staff;
import com.example.shopGiay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String listStaff(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Staff> staffPage = staffService.getAllStaff(pageable);
        model.addAttribute("staffPage", staffPage);
        return "staff/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("staff", new Staff());
        model.addAttribute("isNew", true);
        return "staff/form";
    }

    @PostMapping("/new")
    public String saveStaff(@Valid @ModelAttribute Staff staff, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", true);
            return "staff/form";
        }
        staffService.saveStaff(staff);
        return "redirect:/staff";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Staff staff = staffService.getStaffById(id).orElseThrow(() -> new IllegalArgumentException("Invalid staff Id:" + id));
        model.addAttribute("staff", staff);
        model.addAttribute("isNew", false);
        return "staff/form";
    }

    @PostMapping("/edit/{id}")
    public String updateStaff(@PathVariable("id") Integer id, @Valid @ModelAttribute Staff staff, BindingResult result, Model model) {
        if (result.hasErrors()) {
            staff.setId(id);
            model.addAttribute("isNew", false);
            return "staff/form";
        }
        staffService.saveStaff(staff);
        return "redirect:/staff";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable("id") Integer id) {
        staffService.deleteStaffById(id);
        return "redirect:/staff";
    }
}
