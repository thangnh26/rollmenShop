package com.example.shopGiay.service.impl;

import com.example.shopGiay.dto.SearchOrder;
import com.example.shopGiay.model.*;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.OrderService;
import com.example.shopGiay.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    private VoucherService voucherService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Page<Order> getOrderByStatusNot2(Pageable pageable) {
        return orderRepository.findByStatusNot(2, pageable);
    }

    @Override
    public Page<Order> searchOrderByCustomerCode(String codeCustomer, String phone, Integer status, Pageable pageable, int cusId) {
        return orderRepository.findByCustomer(codeCustomer,phone,status, pageable, cusId);
    }

    @Override
    public Page<Order> searchOrder(String codeCustomer, String phone, Integer status, Pageable pageable) {
        return orderRepository.findByCustomerCodeContainingIgnoreCaseAndStatusNot(codeCustomer,phone,status, pageable);
    }

    @Override
    public Order createOrder(String nameReceiver, String phoneReceiver, String addressReceiver, String note, String price, Integer id, Integer voucherId) {
        Order order = new Order();
        Customer customer = customerRepository.getById(id);
        order.setCustomer(customer);

        // Generate random order code
        Random random = new Random();
        int randomDigits = 100 + random.nextInt(900);
        order.setCode("HD" + randomDigits);

        // Set order details
        order.setCreateDate(LocalDate.now());
        order.setNameReceiver(nameReceiver);
        order.setPhoneReceiver(phoneReceiver);
        order.setAddressReceiver(addressReceiver);
        order.setTotalAmount(Double.parseDouble(price));
        order.setNote(note);
        order.setStatus(0); // Initial status: not confirmed

        // Set the voucher if provided
        if (voucherId != null) {
            Voucher voucher = voucherService.getVoucherById(voucherId);  // Ensure voucherService is not null
            if (voucher != null) {
                order.setVoucher(voucher);
            }
        }

        // Save the order
        orderRepository.save(order);

        return order;
    }


    @Override
    public void confirmPayment(Integer id) {
        Order order = orderRepository.findByIdOder(id);
        List<OrderDetail> list = orderDetailRepository.findAllByOrderId(order.getId());

        // Check if there's enough quantity for all products in the order
        for (OrderDetail orderDetail : list) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            if (orderDetail.getQuantity() > productDetail.getQuantity()) {
                throw new NotEnoughStockException("Không đủ số lượng cho sản phẩm: " + productDetail.getProduct().getName());
            }
        }

        // Deduct quantities if all checks pass
        for (OrderDetail orderDetail : list) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() - orderDetail.getQuantity());
            productDetailRepository.save(productDetail);
        }

        // Confirm the order
        orderRepository.confirm(id, 1);
    }
    public class NotEnoughStockException extends RuntimeException {
        public NotEnoughStockException(String message) {
            super(message);
        }
    }

    @Override
    public void cancelled(Integer id) {
        Order order = orderRepository.findByIdOder(id);
        List<OrderDetail> list = orderDetailRepository.findAllByOrderId(order.getId());

        // Deduct quantities if all checks pass
        for (OrderDetail orderDetail : list) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            productDetailRepository.save(productDetail);
        }
        orderRepository.confirm(id,4);
    }

    @Override
    public void shipping(Integer id) {
        orderRepository.confirm(id,2);
    }

    @Override
    public void ok(Integer id) {
        orderRepository.confirm(id,3);
    }

    @Override
    @PreAuthorize("hasAnyRole('STAFF') or hasRole('ADMIN')")
    public BigDecimal total() {
        return BigDecimal.valueOf(orderRepository.total()!=null ? orderRepository.total():0.0);
    }
}
