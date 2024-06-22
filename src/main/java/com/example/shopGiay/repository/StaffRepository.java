package com.example.shopGiay.repository;

import com.example.shopGiay.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository  extends JpaRepository<Staff, Integer> {
    Page<Staff> findByStatusNot(int status, Pageable pageable);
    Page<Staff> findByCodeContainingIgnoreCaseAndStatusNot(String code, Integer status, Pageable pageable);
}
