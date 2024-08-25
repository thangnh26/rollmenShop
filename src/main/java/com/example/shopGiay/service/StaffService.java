package com.example.shopGiay.service;

import com.example.shopGiay.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();
    List<Staff> getAllStaffByRole();
    Staff getStaffById(Integer id);
    Staff saveStaff(Staff staff);
    void deleteStaffById(Integer id);
    public Page<Staff> getStaffByStatusNot2(Pageable pageable);
    public Page<Staff> searchStaffByCode(String keyword, Pageable pageable);

    Staff findByEmail(String email);
}
