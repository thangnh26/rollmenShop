package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Staff;
import com.example.shopGiay.repository.StaffRepository;
import com.example.shopGiay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Staff> getAllStaffByRole() {
        return staffRepository.findAllByRole();
    }

    @Override
    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public void deleteStaffById(Integer id) {
         staffRepository.deleteById(id);
    }

    @Override
    public Page<Staff> getStaffByStatusNot2(Pageable pageable) {
        return staffRepository.findByStatusNot(2,pageable);
    }

    @Override
    public Page<Staff> searchStaffByCode(String keyword, Pageable pageable) {
        return staffRepository.findByCodeContainingIgnoreCaseAndStatusNot(keyword,2,pageable);
    }

    @Override
    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
}
