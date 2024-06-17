package com.example.shopGiay.controller;

import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import com.example.shopGiay.service.MaterialService;
import com.example.shopGiay.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        // Retrieve paginated list of materials
        Page<Voucher> voucherPage = voucherService.getAllVoucherPaginated(pageable);

        // Add pagination information to the model
        model.addAttribute("vouchers", voucherPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", voucherPage.getTotalPages());

        return "voucher/list";
    }

    @GetMapping("/new")
    public String createVoucherForm(Model model) {
        Voucher voucher = new Voucher();
        model.addAttribute("voucher", voucher);
        return "voucher/new";
    }

    @PostMapping
    public String saveVoucher(@ModelAttribute("voucher") Voucher voucher) {
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

    @GetMapping("/edit/{id}")
    public String editVoucherForm(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "voucher/edit";
    }


    @PostMapping("/{id}")
    public String updateVoucher(@PathVariable Integer id, @ModelAttribute("voucher") Voucher voucher, Model model) {
        Voucher existingVoucher = voucherService.getVoucherById(id);
        existingVoucher.setId(id);
        existingVoucher.setCreateDate(voucher.getCreateDate());
        existingVoucher.setUpdateDate(voucher.getUpdateDate());
        existingVoucher.setValue(voucher.getValue());
        existingVoucher.setQuantity(voucher.getQuantity());
        existingVoucher.setNameVoucher(voucher.getNameVoucher());
        existingVoucher.setStartDate(voucher.getStartDate());
        existingVoucher.setEndDate(voucher.getEndDate());
        existingVoucher.setStatus(voucher.getStatus());
        voucherService.saveVoucher(existingVoucher);
        return "redirect:/voucher";
    }

    @GetMapping("/{id}")
    public String deleteVoucher(@PathVariable Integer id) {
        Voucher voucher =voucherService.getVoucherById(id);
        voucher.setStatus(0);
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

}
