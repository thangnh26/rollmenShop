package com.example.shopGiay.service;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    Voucher getVoucherById(Integer id);
    Voucher saveVoucher(Voucher voucher);
    Page<Voucher> getVoucherByStatusNot2(Pageable pageable);
    Page<Voucher> searchVoucherByName(String keyword, Pageable pageable);
}
