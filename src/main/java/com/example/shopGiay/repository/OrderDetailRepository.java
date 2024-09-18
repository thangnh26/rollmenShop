package com.example.shopGiay.repository;


import com.example.shopGiay.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM dbshopgiay.order_detail WHERE order_id = ?1")
    List<OrderDetail> findAllByOrderId(int orderId);

    List<OrderDetail> findByOrderIdIn(List<Integer> orderIds);

    List<OrderDetail> findByOrderId(Integer orderId);
//
//    @Query(value = "select od from OrderDetail od join Order o on o.id=od.order.id where o.id = :id")
//    List<OrderDetail> findAllByOrderId(Integer id);
}
