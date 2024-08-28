package com.example.shopGiay.controller;

import com.example.shopGiay.dto.SearchOrder;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.UserRepository;
import com.example.shopGiay.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    private StaffService staffService; // Đối tượng Service cho bảng Staff

    @Autowired
    private CustomerService customerService; // Đối tượng Service cho bảng Customer
    @Autowired
    private VoucherService voucherService; // Đối tượng Service cho bảng Customer
    @Autowired
    ProductService productService;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    BrandService brandService;
    @Autowired
    UserRepository userRepository;

    private final OrderDetailService orderDetailService;

    public OrderController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String codeCustomer,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false)Integer status) {

        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepository.findByEmail(email);

        Page<Order> orders;
//        if (keyword != null && !keyword.isEmpty()) {
            orders = orderService.searchOrder(codeCustomer,phone,status, pageable);
        List<Integer> orderIds = orders.getContent().stream()
                .map(Order::getId) // Assuming Order class has getId() method
                .collect(Collectors.toList());
        List<OrderDetail> orderDetail = orderDetailService.getByIds(orderIds);
//        } else {
//            orders = orderService.getOrderByStatusNot2(pageable);
//        }

        // Add pagination information to the model

        model.addAttribute("orders", orders.getContent());
        model.addAttribute("size", orderDetail.stream());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("codeCustomer", codeCustomer); // Pass keyword back to the view
        model.addAttribute("phone", phone); // Pass keyword back to the view
        model.addAttribute("status", status); // Pass keyword back to the view

        return "order/list";
    }
    @GetMapping("/history")
    public String listOrdersCustomer(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String codeCustomer,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false)Integer status, HttpServletRequest request) {

        // Define pagination parameters
        int pageSize = 5; // Number of items per page
        Pageable pageable = PageRequest.of(page, pageSize);
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        // Lấy User từ email
        Customer user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }
        Page<Order> orders;
//        if (keyword != null && !keyword.isEmpty()) {
        orders = orderService.searchOrderByCustomerCode(codeCustomer,phone,status, pageable,user.getId());
//        } else {
//            orders = orderService.getOrderByStatusNot2(pageable);
//        }

        // Add pagination information to the model

        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("codeCustomer", codeCustomer); // Pass keyword back to the view
        model.addAttribute("phone", phone); // Pass keyword back to the view
        model.addAttribute("status", status); // Pass keyword back to the view

        return "order/listOrderHistory";
    }

    @GetMapping("/confirm-payment/{id}")
    public String confirmPayment(@PathVariable Integer id){
        orderService.confirmPayment(id);
        return "redirect:/order";
    }

    @GetMapping("/cancelled/{id}")
    public String cancelled(@PathVariable Integer id){
        orderService.cancelled(id);
        return "redirect:/order";
    }
    @GetMapping("/cancelled-customer/{id}")
    public String cancelledCustomer(@PathVariable Integer id){
        orderService.cancelled(id);
        return "redirect:/user/profile";
    }

    @GetMapping("/shipping/{id}")
    public String shipping(@PathVariable Integer id){
        orderService.shipping(id);
        return "redirect:/order";
    }

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable Integer id){
        orderService.ok(id);
        return "redirect:/order";
    }

    @GetMapping("/new")
    public String createOrderForm(Model model) {
        Order order = new Order();
        List<Staff> staffList = staffService.getAllStaff();
        List<Customer> customerList = customerService.getAllCustomer();
        List<Voucher> voucherList = voucherService.getAllVoucher();

        model.addAttribute("order", order);
        model.addAttribute("staffList", staffList);
        model.addAttribute("customerList", customerList);
        model.addAttribute("voucherList", voucherList);

        return "order/new";
    }


    @PostMapping
    public String saveOrder(@Valid @ModelAttribute("order") Order order,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "order/new";
        }
        order.setCreateDate(LocalDate.now());
        orderService.saveOrder(order);
        return "redirect:/order";
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        List<Staff> staffList = staffService.getAllStaff();
        List<Customer> customerList = customerService.getAllCustomer();
        List<Voucher> voucherList = voucherService.getAllVoucher();

        model.addAttribute("order", order);
        model.addAttribute("staffList", staffList);
        model.addAttribute("customerList", customerList);
        model.addAttribute("voucherList", voucherList);
        return "order/edit";
    }

    @PostMapping("/{id}")
    public String updateOrder(@PathVariable Integer id,
                              @Valid @ModelAttribute("order") Order order,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "order/edit";
        }
        Order existingOrder = orderService.getOrderById(id);
        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setStaff(order.getStaff());
        existingOrder.setVoucher(order.getVoucher());
        existingOrder.setNote(order.getNote());
        existingOrder.setAddressReceiver(order.getAddressReceiver());
        existingOrder.setNameReceiver(order.getNameReceiver());
        existingOrder.setPhoneReceiver(order.getPhoneReceiver());
        existingOrder.setTotalAmount(order.getTotalAmount());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setUpdateDate(LocalDate.now()); // Cập nhật ngày update
        orderService.saveOrder(existingOrder); // Lưu lại đơn hàng đã cập nhật
        return "redirect:/order";
    }

    @GetMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        Order order = orderService.getOrderById(id);
        order.setStatus(2);
        orderService.saveOrder(order);
        return "redirect:/order";
    }
}
