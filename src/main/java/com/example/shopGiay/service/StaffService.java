package com.example.shopGiay.service;

import com.example.shopGiay.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface StaffService {
    Page<Staff> getAllStaff(Pageable pageable);
    Optional<Staff> getStaffById(Integer id);
    Staff saveStaff(Staff staff);
    void deleteStaffById(Integer id);
}
