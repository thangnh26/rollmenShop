package com.example.shopGiay.repository;


import com.example.shopGiay.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM dbshopgiay.order_details WHERE order_id = ?1")
    List<OrderDetail> findAllByOrderId(int orderId);
    @Query(nativeQuery = true, value = "DELETE FROM dbshopgiay.order_details WHERE order_id = ?1")
    void deleteOrderDetail(int id);

    @Query(value = "select sum(od.quantity) from OrderDetail od where od.order.id=:id")
    Integer sumQuantity(int id);
}
