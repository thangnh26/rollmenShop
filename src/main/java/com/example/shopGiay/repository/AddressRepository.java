package com.example.shopGiay.repository;

import com.example.shopGiay.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query(value = "select * from dbshopgiay.address where customer_id = :cusId order by id asc limit 1",nativeQuery = true)
    Address findByCustomerId(int cusId);

    @Query(value = "select a from Address a where a.customer.id=:id")
    List<Address> findByCusId(Integer id);

    @Query(value = "select a.id from Address a where a.nameAddress=:address")
    Integer findByName(String address);
}