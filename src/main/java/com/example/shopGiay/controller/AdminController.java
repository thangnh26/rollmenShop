package com.example.shopGiay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String adminPage(){
        return "redirect:admin/index";
    }
    @GetMapping("/admin/index")
    public String adminHome(Model model){
//        model.addAttribute("totalProduct",productRepository.countProduct());
//        model.addAttribute("countUser",userRepository.countUser());
//        model.addAttribute("countOrder", orderRepository.countOrder());
//        model.addAttribute("countOrderWait", orderRepository.countDonHangCho());
        return "admin/index";
    }
}
