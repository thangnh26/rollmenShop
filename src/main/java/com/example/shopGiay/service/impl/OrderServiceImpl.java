package com.example.shopGiay.service.impl;

import com.example.shopGiay.dto.SearchOrder;
import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.Order;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.User;
import com.example.shopGiay.repository.CustomerRepository;
import com.example.shopGiay.repository.OrderDetailRepository;
import com.example.shopGiay.repository.OrderRepository;
import com.example.shopGiay.repository.UserRepository;
import com.example.shopGiay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Timestamp;
import java.util.List;

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
    public Order createOrder(String nameReceiver, String phoneReceiver, String addressReceiver, String note, String price, Integer id) {
        Order order = new Order();
        User user = userRepository.getById(id);
        Customer customer = customerRepository.getById(id);
        order.setCustomer(customer);
//        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setCode("HD00"+order.getId());
        order.setNameReceiver(nameReceiver);
        order.setPhoneReceiver(phoneReceiver);
        order.setAddressReceiver(addressReceiver);
        order.setTotalAmount(Double.parseDouble(price));
        order.setNote(note);
        order.setStatus(0);
        orderRepository.save(order);
        user.setStatus(true);
        return order;
    }

    @Override
    public void confirmPayment(Integer id) {
        Order order = orderRepository.findByIdOder(id);
        List<OrderDetail> list = orderDetailRepository.findAllByOrderId(order.getId());
        for (OrderDetail listOrder : list){
            if (listOrder.getQuantity() > listOrder.getProductDetail().getQuantity()){
                throw new NotFoundException("không đủ số lượng cho sản phẩm: " + listOrder.getProductDetail().getProduct().getName());
            }
        }
        orderRepository.confirm(id,1);
    }

    @Override
    public void cancelled(Integer id) {
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
}
