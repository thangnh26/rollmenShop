package com.example.shopGiay.service;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllVoucher();
    Voucher getVoucherById(Integer id);
    Voucher saveVoucher(Voucher voucher);
    void deleteVoucherById(Integer id);
    Page<Voucher> getAllVoucherPaginated(Pageable pageable);
}
