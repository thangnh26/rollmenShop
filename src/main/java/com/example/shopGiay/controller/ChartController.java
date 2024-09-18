package com.example.shopGiay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {
    @GetMapping("/chart")
    public String getChart(Model model) {
        // Sample data
        String[] labels = {"January", "February", "March", "April", "May"};
        int[] data = {10, 20, 30, 40, 50};

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "admin/chart"; // The name of your Thymeleaf template
    }
}
