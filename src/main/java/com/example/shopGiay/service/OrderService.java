package com.example.shopGiay.service;

import com.example.shopGiay.dto.SearchOrder;
import com.example.shopGiay.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Integer id);
    Order saveOrder(Order order);
    void deleteOrderById(Integer id);
    Page<Order> getOrderByStatusNot2(Pageable pageable);
    Page<Order> searchOrderByCustomerCode(String codeCustomer, String phone, Integer status, Pageable pageable, int cusId);
    Page<Order> searchOrder(String codeCustomer, String phone, Integer status, Pageable pageable);

    Order createOrder(String nameReceiver, String phoneReceiver, String addressReceiver, String note, String price, Integer id);

    void confirmPayment(Integer id);

    void cancelled(Integer id);

    void shipping(Integer id);

    void ok(Integer id);

    BigDecimal total();


}
