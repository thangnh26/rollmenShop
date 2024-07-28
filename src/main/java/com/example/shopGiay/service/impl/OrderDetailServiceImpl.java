package com.example.shopGiay.service.impl;


import com.example.shopGiay.model.Order;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.repository.*;
import com.example.shopGiay.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;
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
        Product product = productRepository.getById(productId);
        ProductDetail productDetail = productDetailRepository.getOneProductDetail(productId,colorId,size);
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(productDetail);
        orderDetail.setQuantity(qty);

        BigDecimal price = productDetail.getPrice().multiply(BigDecimal.valueOf(qty));
        orderDetail.setPrice(price.doubleValue());
//        orderDetail.setSize(size);


//        productRepository.save(product);
        orderDetail.setStatus(order.getStatus());
        orderDetailRepository.save(orderDetail);
        return orderDetail;
    }
}
