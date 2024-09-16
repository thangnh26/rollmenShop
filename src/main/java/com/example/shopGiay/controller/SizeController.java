package com.example.shopGiay.controller;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.Size;
import com.example.shopGiay.service.SizeService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sizes")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public String listSizes(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int sizePage = size.orElse(5);
        // Đảm bảo currentPage không nhỏ hơn 1
        if (currentPage < 1) {
            currentPage = 1;
        }
        Pageable pageable = PageRequest.of(currentPage - 1, sizePage);
        Page<Size> listSize = sizeService.findAllOrderById(pageable);
        model.addAttribute("sizes", listSize);
        int totalPage = listSize.getTotalPages();
        if (totalPage > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPage);
            if (totalPage > 5) {
                if (end == totalPage) {
                    start = end - 5;
                } else if (start == 1) {
                    end = start + 5;
                }
            }
            List<Integer> pageNumber = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumber", pageNumber);
        }

        return "admin/sizes/list";
    }
    @GetMapping("/new")
    public String createSizeForm(Model model) {
        Size size = new Size();
        model.addAttribute("size", size);
        return "admin/sizes/new";
    }
    @PostMapping
    public String saveSize(@Valid @ModelAttribute("size") Size size, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/sizes/new";
        }
        if (sizeService.existsBySizeNumber(size.getSizeNumber())) {
            result.rejectValue("sizeNumber", "error.sizeNumber", "Kích thước đã tồn tại");
            return "admin/sizes/new";
        }
        size.setStatus(1);
        size.setCreateDate(LocalDate.now());
        sizeService.saveSize(size);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành công!");
        return "redirect:/sizes";
    }

    @GetMapping("/edit/{id}")
    public String editSizeForm(@PathVariable Integer id, Model model) {
        Size size = sizeService.getSizeById(id);
        model.addAttribute("size", size);
        return "admin/sizes/edit";
    }

    @PostMapping("/{id}")
    public String updateSize(@PathVariable Integer id,@Valid @ModelAttribute("size") Size size, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/sizes/edit";
        }
        if (sizeService.existsBySizeNumberAndIdNot(size.getSizeNumber(), size.getId())) {
            result.rejectValue("sizeNumber", "error.sizeNumber", "Kích thước đã tồn tại");
            return "admin/sizes/edit";
        }
        Size existingSize = sizeService.getSizeById(id);
        existingSize.setSizeNumber(size.getSizeNumber());
        existingSize.setUpdateDate(LocalDate.now());
        existingSize.setStatus(size.getStatus());
        sizeService.saveSize(existingSize);
        redirectAttributes.addFlashAttribute("successMessage", "Sửa mới thành công!");
        return "redirect:/sizes";
    }

    @GetMapping("/{id}")
    public String deleteSize(@PathVariable Integer id) {
        sizeService.deleteSizeById(id);
        return "redirect:/sizes";
    }
}
