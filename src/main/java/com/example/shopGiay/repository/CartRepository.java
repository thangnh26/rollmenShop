package com.example.shopGiay.repository;

import com.example.shopGiay.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "select c from Cart c where c.customer.id=:cusId and c.isCheck=0")
    Cart getOneByCusId(int cusId);
}
