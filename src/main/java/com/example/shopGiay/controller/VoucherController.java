package com.example.shopGiay.controller;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import com.example.shopGiay.service.MaterialService;
import com.example.shopGiay.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        return "voucher/list";
    }

    @GetMapping("/new")
    public String createVoucherForm(Model model) {
        Voucher voucher = new Voucher();
        model.addAttribute("voucher", voucher);
        return "voucher/new";
    }

    @PostMapping("/add")
    public String addVoucher(@Valid @ModelAttribute("voucher") Voucher voucher, BindingResult result) {
        if (result.hasErrors()) {
            return "voucher/new"; // Trả về lại form nếu có lỗi validation
        }
        voucher.setCreateDate(LocalDate.now());
        // Lưu category vào cơ sở dữ liệu nếu hợp lệ
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

    @GetMapping("/edit/{id}")
    public String editVoucherForm(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "voucher/edit"; // This should match the name of your Thymeleaf template
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Integer id,
                                 @Valid @ModelAttribute("voucher") Voucher voucher,
                                 BindingResult result) {
        // Kiểm tra xem có lỗi validation không
        if (result.hasErrors()) {
            // Nếu có lỗi, trả về lại form chỉnh sửa với các thông tin lỗi
            return "voucher/edit";
        }

        Voucher existingVoucher = voucherService.getVoucherById(id);
        existingVoucher.setId(id);
        existingVoucher.setValue(voucher.getValue());
        existingVoucher.setQuantity(voucher.getQuantity());
        existingVoucher.setNameVoucher(voucher.getNameVoucher());
        existingVoucher.setStartDate(voucher.getStartDate());
        existingVoucher.setEndDate(voucher.getEndDate());
        existingVoucher.setStatus(voucher.getStatus());
        existingVoucher.setUpdateDate(LocalDate.now());
        voucherService.saveVoucher(existingVoucher);
        // Lưu lại danh mục đã chỉnh sửa vào cơ sở dữ liệu
        voucherService.saveVoucher(existingVoucher);
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
