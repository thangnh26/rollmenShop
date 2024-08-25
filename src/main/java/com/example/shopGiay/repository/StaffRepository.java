package com.example.shopGiay.repository;

import com.example.shopGiay.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository  extends JpaRepository<Staff, Integer> {
    Page<Staff> findByStatusNot(int status, Pageable pageable);
    Page<Staff> findByCodeContainingIgnoreCaseAndStatusNot(String code, Integer status, Pageable pageable);

    @Query("SELECT u FROM Staff u WHERE u.email = ?1")
    Staff findByEmail(String email);

    @Query(value = "SELECT * FROM staff u join staff_roles sr on u.id = sr.staff_id  WHERE sr.role_id=1 ",nativeQuery = true)
    List<Staff> findAllByRole();
}
