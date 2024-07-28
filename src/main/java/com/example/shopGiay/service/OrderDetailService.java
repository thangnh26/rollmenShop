package com.example.shopGiay.service;

import com.example.shopGiay.model.OrderDetail;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService {

    OrderDetail createOrderDetail(int productId, int orderId , int qty, int size, int colorId);

}
