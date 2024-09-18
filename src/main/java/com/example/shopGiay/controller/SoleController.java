package com.example.shopGiay.controller;


import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Sole;
import com.example.shopGiay.service.SoleService;
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
@RequestMapping("/soles")
public class SoleController {
    @Autowired
    private SoleService soleService;

    @GetMapping
    public String listSoles(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String keyword) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials with status 1 or 0
        Page<Sole> solePage;
        if (keyword != null && !keyword.isEmpty()) {
            solePage = soleService.searchSoleByName(keyword, pageable);
        } else {
            solePage = soleService.getSoleByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("soles", solePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", solePage.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view

        return "admin/soles/list";
    }

    @GetMapping("/new")
    public String createSoleForm(Model model) {
        Sole sole = new Sole();
        model.addAttribute("sole", sole);
        return "admin/soles/new";
    }

    @PostMapping
    public String saveSole(@Valid @ModelAttribute("sole") Sole sole, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/soles/new"; // Trả về lại form nếu có lỗi validation
        }
        if (soleService.existsByName(sole.getName())) {
            // Nếu đã tồn tại, thêm thông báo lỗi và trả về lại form
            result.rejectValue("name", "error.name", "Tên đế đã tồn tại!");
            return "admin/soles/new";
        }
        sole.setStatus(1);
        sole.setCreateDate(LocalDate.now());
        soleService.saveSole(sole);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành công!");
        return "redirect:/soles";
    }

    @GetMapping("/edit/{id}")
    public String editSoleForm(@PathVariable Integer id, Model model) {
        Sole sole = soleService.getSoleById(id);
        model.addAttribute("sole", sole);
        return "admin/soles/edit";
    }

    @PostMapping("/{id}")
    public String updateSole(@PathVariable Integer id,
                             @Valid @ModelAttribute("sole") Sole sole, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/soles/edit";
        }
        if (soleService.existsByNameAndIdNot(sole.getName(), sole.getId())) {
            result.rejectValue("name", "error.name", "Tên đế đã tồn tại!");
            return "admin/soles/edit";
        }
        Sole Soletc = soleService.getSoleById(id);
        Soletc.setId(id);
        Soletc.setName(sole.getName());
        Soletc.setUpdateDate(LocalDate.now());
        Soletc.setStatus(sole.getStatus());
        soleService.saveSole(Soletc);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa thành công!");
        return "redirect:/soles";
    }

    @GetMapping("/{id}")
    public String deleteSole(@PathVariable Integer id) {
        soleService.deleteSoleById(id);
        return "redirect:/soles";
    }
}
