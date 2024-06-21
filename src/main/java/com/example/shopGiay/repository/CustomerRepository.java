package com.example.shopGiay.repository;

import com.example.shopGiay.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findByStatusNot(int status, Pageable pageable);
    Page<Customer> findByCodeContainingIgnoreCaseAndStatusNot(String code, Integer status, Pageable pageable);
}
