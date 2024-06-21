package com.example.shopGiay.controller;
import com.example.shopGiay.model.Address;
import com.example.shopGiay.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public String getAllAddresses(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5); // 5 items per page
        Page<Address> addressPage = addressService.getAllAddresses(pageable);
        model.addAttribute("addressPage", addressPage);
        return "address/list";
    }

    @GetMapping("/add")
    public String showAddForm(Address address) {
        return "address/add";
    }

    @PostMapping("/add")
    public String addAddress(@Valid Address address, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "address/add";
        }
        addressService.saveAddress(address);
        return "redirect:/address";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Address address = addressService.getAddressById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid address Id:" + id));
        model.addAttribute("address", address);
        return "address/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable("id") Integer id, @Valid Address address, BindingResult result, Model model) {
        if (result.hasErrors()) {
            address.setId(id);
            return "address/edit";
        }
        addressService.saveAddress(address);
        return "redirect:/address";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer id, Model model) {
        addressService.deleteAddress(id);
        return "redirect:/address";
    }
}
