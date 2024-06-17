package com.example.shopGiay.repository;

import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>  {

}
