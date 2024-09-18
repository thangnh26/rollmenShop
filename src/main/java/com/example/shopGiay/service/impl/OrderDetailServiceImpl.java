package com.example.shopGiay.service.impl;


import com.example.shopGiay.model.Order;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.OrderDetailService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    EmailService emailService;

//
//    @Autowired
//    ProductSizeRepository productSizeRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public OrderDetail createOrderDetail(int productId, int orderId, int qty, int size, int colorId){
        OrderDetail orderDetail = new OrderDetail();
        Order order = orderRepository.getById(orderId);
//        Product product = productRepository.getById(productId);
        ProductDetail productDetail = productDetailRepository.getOneProductDetail(productId,colorId,size);
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(productDetail);
        orderDetail.setQuantity(qty);

        BigDecimal price = productDetail.getPrice().multiply(BigDecimal.valueOf(qty));
        orderDetail.setPrice(price.doubleValue());
        orderDetail.setStatus(order.getStatus());
        orderDetailRepository.save(orderDetail);
        String to = order.getCustomer().getEmail();
        String subject = "Đơn hàng "+ order.getCode();
        String text = "Dear: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() + ",\n" +
                "Cảm ơn quý khách đã đặt hàng tại RollMen Shop. Chúng tôi xin xác nhận rằng đơn hàng của quý khách đã được xử lý thành công với các thông tin sau:\n\n" +
                "Tên Khách Hàng: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() + "\n" +
                "Số Điện Thoại: " + order.getPhoneReceiver() + "\n" +
                "Địa Chỉ Giao Hàng: " + order.getAddressReceiver() + "\n" +
                "Mã Hóa Đơn: " + order.getCode() + "\n" +
                "Sản Phẩm Đã Đặt: " + orderDetail.getProductDetail().getProduct().getName();
        emailService.sendEmail(to,subject,text);
        return orderDetail;
    }

    @Override
    public List<OrderDetail> getById(Integer id) {
        Order order = orderRepository.findByIdOder(id);
        System.out.println(order);
        return orderDetailRepository.findAllByOrderId(id);
    }

    @Override
    public Order getOrderById(Integer id) {
        Order order = orderRepository.findByIdOder(id);
        return order;
    }

    @Override
    public List<OrderDetail> getByIds(List<Integer> orderIds) {
        return orderDetailRepository.findByOrderIdIn(orderIds);
    }


    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
