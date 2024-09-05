package com.example.shopGiay.controller;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import com.example.shopGiay.service.MaterialService;
import com.example.shopGiay.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping
    public String listVoucher(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String keyword) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials with status 1 or 0
        Page<Voucher> voucherPage;
        if (keyword != null && !keyword.isEmpty()) {
            voucherPage = voucherService.searchVoucherByName(keyword, pageable);
        } else {
            voucherPage = voucherService.getVoucherByStatusNot2(pageable);
        }
        // Add pagination information to the model
        model.addAttribute("vouchers", voucherPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", voucherPage.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view
        return "admin/voucher/list";
    }

    @GetMapping("/new")
    public String createVoucherForm(Model model) {
        Voucher voucher = new Voucher();
        model.addAttribute("voucher", voucher);
        return "admin/voucher/new";
    }

    @PostMapping("/add")
    public String addVoucher(@Valid @ModelAttribute("voucher") Voucher voucher, BindingResult result) {
        // Kiểm tra lỗi xác thực (như tên trống)
        if (result.hasErrors()) {
            return "admin/voucher/new"; // Trả về lại form nếu có lỗi validation
        }

        // Kiểm tra lỗi trùng lặp tên voucher
        try {
            voucher.setStatus(1);
            voucher.setCreateDate(LocalDate.now());
            voucherService.saveVoucher(voucher);
        } catch (IllegalArgumentException e) {
            // Thêm lỗi vào BindingResult để hiển thị trong form
            result.rejectValue("nameVoucher", "error.voucher", e.getMessage());
            return "admin/voucher/new"; // Trả về lại form nếu tên voucher đã tồn tại
        }

        // Nếu không có lỗi, chuyển hướng đến danh sách voucher
        return "redirect:/voucher";
    }


    @GetMapping("/edit/{id}")
    public String editVoucherForm(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "admin/voucher/edit"; // This should match the name of your Thymeleaf template
    }

    @PostMapping("/edit/{id}")
    public String updateVoucher(@PathVariable Integer id,
                                @Valid @ModelAttribute("voucher") Voucher voucher,
                                BindingResult result) {
        // Kiểm tra lỗi xác thực
        if (result.hasErrors()) {
            return "admin/voucher/edit"; // Trả về lại form nếu có lỗi validation
        }

        // Lấy voucher hiện tại từ cơ sở dữ liệu
        Voucher existingVoucher = voucherService.getVoucherById(id);
        if (existingVoucher == null) {
            result.rejectValue("nameVoucher", "error.voucher", "Voucher không tồn tại");
            return "admin/voucher/edit"; // Trả về lại form nếu không tìm thấy voucher
        }

        // Kiểm tra lỗi trùng lặp tên voucher
        if (voucherService.existsByNameVoucherAndNotId(voucher.getNameVoucher(), id)) {
            result.rejectValue("nameVoucher", "error.voucher", "Tên voucher đã tồn tại");
            return "admin/voucher/edit"; // Trả về lại form nếu tên voucher đã tồn tại
        }

        // Cập nhật các thông tin voucher
        existingVoucher.setValue(voucher.getValue());
        existingVoucher.setQuantity(voucher.getQuantity());
        existingVoucher.setNameVoucher(voucher.getNameVoucher());
        existingVoucher.setStartDate(voucher.getStartDate());
        existingVoucher.setEndDate(voucher.getEndDate());
        existingVoucher.setStatus(voucher.getStatus());
        existingVoucher.setUpdateDate(LocalDate.now());

        // Lưu voucher đã cập nhật vào cơ sở dữ liệu
        voucherService.updateVoucher(existingVoucher);

        // Chuyển hướng đến danh sách voucher
        return "redirect:/voucher";
    }



    @GetMapping("/{id}")
    public String deleteVoucher(@PathVariable Integer id) {
        Voucher voucher = voucherService.getVoucherById(id);
        voucher.setStatus(2); // Set status to 2 to mark as deleted
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

}
