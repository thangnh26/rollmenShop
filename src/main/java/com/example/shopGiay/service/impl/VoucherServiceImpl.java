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
        if (voucherRepository.existsByNameVoucher(voucher.getNameVoucher())) {
            throw new IllegalArgumentException("Tên voucher đã tồn tại.");
        }
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(Voucher voucher) {
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

    @Override
    public boolean existsByNameVoucherAndNotId(String nameVoucher, Integer id) {
        return voucherRepository.existsByNameVoucherAndIdNot(nameVoucher, id);
    }
    @Override
    public Voucher reduceVoucherQuantity(Integer voucherId, int quantityToReduce) {
        // Find the voucher by ID or throw an exception if not found
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        // Reduce the quantity
        int newQuantity = voucher.getQuantity() - quantityToReduce;

        // Ensure quantity is not negative
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient voucher quantity.");
        }

        voucher.setQuantity(newQuantity);
        voucherRepository.save(voucher);  // Only updating quantity
        return voucher;
    }
}
