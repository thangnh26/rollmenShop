package com.example.shopGiay.controller;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.Roles;
import com.example.shopGiay.model.Staff;
import com.example.shopGiay.repository.RolesRepository;
import com.example.shopGiay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;
    @Autowired
    private RolesRepository roleRepository;
    @GetMapping
    public String listStaff(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) String keyword) {

        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Staff> staff;
        if (keyword != null && !keyword.isEmpty()) {
            staff = staffService.searchStaffByCode(keyword, pageable);
        } else {
            staff = staffService.getStaffByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("staff", staff.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", staff.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view

        return "staff/list";
    }
    @GetMapping("/new")
    public String createStaffForm(Model model) {
        Staff staff = new Staff();

        // Lấy tất cả các vai trò từ RoleRepository
        List<Roles> allRoles = roleRepository.findAll();

        // Đưa staff mới và danh sách roles vào model
        model.addAttribute("staff", staff);
        model.addAttribute("allRoles", allRoles);
        return "staff/new";
    }

    @PostMapping
    public String saveStaff(@Valid @ModelAttribute("staff") Staff staff,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Roles> allRoles = roleRepository.findAll();
            model.addAttribute("allRoles", allRoles);
            return "staff/new";
        }
        staff.setStatus(1);
        staff.setCreateDate(LocalDate.now());
        staffService.saveStaff(staff);
        return "redirect:/staff";
    }
    @GetMapping("/edit/{id}")
    public String editStaffForm(@PathVariable Integer id, Model model) {
        // Lấy thông tin nhân viên theo ID
        Staff staff = staffService.getStaffById(id);

        // Lấy danh sách tất cả vai trò
        List<Roles> allRoles = roleRepository.findAll();

        // Thêm thông tin vào model
        model.addAttribute("staff", staff);
        model.addAttribute("allRoles", allRoles);
        return "staff/edit";
    }
    @PostMapping("/{id}")
    public String updateStaff(@PathVariable Integer id,
                                 @Valid @ModelAttribute("staff") Staff staff,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "staff/edit";
        }
        Staff existingStaff = staffService.getStaffById(id);

        // Cập nhật các thuộc tính của nhân viên hiện tại từ thông tin của form
        existingStaff.setCode(staff.getCode());
        existingStaff.setFirstName(staff.getFirstName());
        existingStaff.setLastName(staff.getLastName());
        existingStaff.setGender(staff.getGender());
        existingStaff.setDescription(staff.getDescription());
        existingStaff.setEmail(staff.getEmail());
        existingStaff.setPassword(staff.getPassword());
        existingStaff.setPhoneNumber(staff.getPhoneNumber());
        existingStaff.setStatus(staff.getStatus());
        existingStaff.setUpdateDate(LocalDate.now());
        // Cập nhật roles từ form
        existingStaff.setRoles(staff.getRoles());
        staffService.saveStaff(existingStaff);
        return "redirect:/staff";
    }

    @GetMapping("/{id}")
    public String deleteStaff(@PathVariable Integer id) {
        Staff staff = staffService.getStaffById(id);
        staff.setStatus(2);
        staffService.saveStaff(staff);
        return "redirect:/staff";
    }
}
