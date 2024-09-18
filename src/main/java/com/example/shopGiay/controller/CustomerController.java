package com.example.shopGiay.controller;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) String keyword) {

        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Customer> customers;
        if (keyword != null && !keyword.isEmpty()) {
            customers = customerService.searchCustomerByCode(keyword, pageable);
        } else {
            customers = customerService.getCustomerByStatusNot2(pageable);
        }

        // Add pagination information to the model
        model.addAttribute("customers", customers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("keyword", keyword); // Pass keyword back to the view

        return "customer/list";
    }

    @GetMapping("/new")
    public String createCustomerForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer/new";
    }

    @PostMapping
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "customer/new";
        }
        customer.setCreateDate(LocalDate.now());
        customerService.saveCustomer(customer);
        return "redirect:/customer";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerForm(@PathVariable Integer id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "customer/edit";
    }

    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Integer id,
                                 @Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "customer/edit";
        }
        Customer existingCustomer = customerService.getCustomerById(id);
        existingCustomer.setCode(customer.getCode());
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setGender(customer.getGender());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setStatus(customer.getStatus());
        existingCustomer.setUpdateDate(LocalDate.now()); // Cập nhật ngày update
        customerService.saveCustomer(existingCustomer); // Lưu lại khách hàng đã cập nhật
        return "redirect:/user/profile";
    }

    @GetMapping("/{id}")
    public String deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        customer.setStatus(2);
        customerService.saveCustomer(customer);
        return "redirect:/customer";
    }
}
