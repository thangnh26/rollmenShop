package com.example.shopGiay.service;

import com.example.shopGiay.model.Order;
import com.example.shopGiay.model.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderDetailService {

    OrderDetail createOrderDetail(int productId, int orderId , int qty, int size, int colorId);

    List<OrderDetail> getById(Integer id);
    Order getOrderById(Integer id);
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
    List<OrderDetail> getByIds(List<Integer> orderIds);
}
