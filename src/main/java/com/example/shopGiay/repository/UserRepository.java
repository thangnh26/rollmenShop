package com.example.shopGiay.repository;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM Customer u WHERE u.email = ?1")
    Customer findByEmail(String email);


}
