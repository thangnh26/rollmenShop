package com.example.shopGiay.controller;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.Staff;
import com.example.shopGiay.repository.OrderRepository;
import com.example.shopGiay.repository.ProductRepository;
import com.example.shopGiay.service.OrderService;
import com.example.shopGiay.service.ProductService;
import com.example.shopGiay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    StaffService staffService;

    @Autowired
    ProductRepository productRepository;

//    @PreAuthorize("hasRole('STAFF')")
//    @GetMapping("/admin")
//    public String adminPage(){
//        return "redirect:admin/index";
//    }

    @GetMapping("/staff/profile")
    public String showUserProfile(Model model, HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        Staff user = staffService.findByEmail(email);

        if (user != null) {
            session.setAttribute("loggedInUser", user);
            model.addAttribute("user", user);
            return "staff/staff_profile";
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
    }

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    @GetMapping("/admin/index")
    public String adminHome(Model model){
        BigDecimal total =orderService.total();
        model.addAttribute("list10spbanchay",productRepository.getProductHotthongke());
        model.addAttribute("total",total);
        model.addAttribute("countUser",orderRepository.countUser());
        model.addAttribute("countOrder", orderRepository.countOrder());
        model.addAttribute("countOrderWait", orderRepository.countDonHangCho());
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("Tháng 1", orderRepository.soLuongTheoThang(1));
        data.put("Tháng 2", orderRepository.soLuongTheoThang(2));
        data.put("Tháng 3", orderRepository.soLuongTheoThang(3));
        data.put("Tháng 4", orderRepository.soLuongTheoThang(4));
        data.put("Tháng 5", orderRepository.soLuongTheoThang(5));
        data.put("Tháng 6", orderRepository.soLuongTheoThang(6));
        data.put("Tháng 7", orderRepository.soLuongTheoThang(7));
        data.put("Tháng 8", orderRepository.soLuongTheoThang(8));
        data.put("Tháng 9", orderRepository.soLuongTheoThang(9));
        data.put("Tháng 10", orderRepository.soLuongTheoThang(10));
        data.put("Tháng 11", orderRepository.soLuongTheoThang(11));
        data.put("Tháng 12", orderRepository.soLuongTheoThang(12));
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());
        return "admin/index";
    }
    @GetMapping("admin/barChart")
    public String barChart(Model model)
    {
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("Tháng 1", 30);
        data.put("Tháng 2", 30);
        data.put("Tháng 3", 30);
        data.put("Tháng 4", 30);
        data.put("Tháng 5", 30);
        data.put("Tháng 6", 30);
        data.put("Tháng 7", 30);
        data.put("Tháng 8", 30);
        data.put("Tháng 9", 30);
        data.put("Tháng 10", 30);
        data.put("Tháng 11", 30);
        data.put("Tháng 12", 30);
        model.addAttribute("keySet", data.keySet());
        model.addAttribute("values", data.values());
        return "admin/index";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/staff")
    public String getAllStaff(Model model){
        List<Staff> staff = staffService.getAllStaffByRole();
        model.addAttribute("staff", staff);
        return "staff/list";
    }
}
