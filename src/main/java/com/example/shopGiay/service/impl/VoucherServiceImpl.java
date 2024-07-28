package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Customer;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Voucher;
import com.example.shopGiay.repository.VoucherRepository;
import com.example.shopGiay.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Page<Voucher> getVoucherByStatusNot2(Pageable pageable) {
        return voucherRepository.findByStatusNot(2, pageable);
    }

    @Override
    public Page<Voucher> searchVoucherByName(String keyword, Pageable pageable) {
        return voucherRepository.findByNameVoucherContainingIgnoreCaseAndStatusNot(keyword, 2, pageable);
    }
}
