package com.example.shopGiay.repository;

import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>  {
    Page<Voucher> findByStatusNot(int status, Pageable pageable);
    Page<Voucher> findByNameVoucherContainingIgnoreCaseAndStatusNot(String nameVoucher, Integer status, Pageable pageable);
    boolean existsByNameVoucher(String nameVoucher);
    boolean existsByNameVoucherAndIdNot(String nameVoucher, Integer id);
}
