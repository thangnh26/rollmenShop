package com.example.shopGiay.repository;

import com.example.shopGiay.dto.SearchOrder;
import com.example.shopGiay.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByStatusNot(int status, Pageable pageable);

    @Query(value = "select od from Order od where " +
//            "od.customer.id = :cusId and " +
            "(:#{#codeCustomer} is null or :#{#codeCustomer}='' or lower(od.code) like lower(concat('%', :#{#codeCustomer}, '%'))) and " +
            "(:#{#status} is null or :#{#status}=od.status) and " +
            "(:#{#phone} is null or :#{#phone}='' or od.phoneReceiver like concat('%', :#{#phone}, '%')) order by od.id desc "
    )
    Page<Order> findByCustomerCodeContainingIgnoreCaseAndStatusNot(String codeCustomer, String phone, Integer status, Pageable pageable);

    @Query(value = "select od from Order od where " +
            "od.customer.id = :cusId and " +
            "(:#{#codeCustomer} is null or :#{#codeCustomer}='' or lower(od.code) like lower(concat('%', :#{#codeCustomer}, '%'))) and " +
            "(:#{#status} is null or :#{#status}=od.status) and " +
            "(:#{#phone} is null or :#{#phone}='' or od.phoneReceiver like concat('%', :#{#phone}, '%')) order by od.id desc "
    )
    Page<Order> findByCustomer(String codeCustomer, String phone, Integer status, Pageable pageable,int cusId);


    @Query(value = "select sum(od.quantity) from `dbshopgiay`.`order_detail` od join `dbshopgiay`.`order` o on o.id = od.order_id where month(o.create_date) = :date and o.status >=1",nativeQuery = true)
    Integer soLuongTheoThang(int date);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `dbshopgiay`.`order` SET `status` = :status WHERE `id` = :id",nativeQuery = true)
    void confirm(Integer id, int status);

    @Query(value = "select count(o) from Order o where o.status=0")
    Integer countDonHangCho();

    @Query(value = "select count(o) from Order o where o.status = 3 ")
    Integer countOrder();

    @Query("select count(distinct o.customer.id) from Order o where o.status = 3")
    Integer countUser();

    @Query(value = "SELECT IFNULL(sum(total_amount), 0.0) FROM `order` WHERE status = 3", nativeQuery = true)
    Double total();



    @Query(value = "select * from `dbshopgiay`.`order` where id=:id",nativeQuery = true)
    Order findByIdOder(Integer id);
}
