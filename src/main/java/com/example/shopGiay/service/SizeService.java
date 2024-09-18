package com.example.shopGiay.service;

import com.example.shopGiay.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SizeService {
    List<Size> getAllSizes();
    Size getSizeById(Integer id);
    Size saveSize(Size size);
    void deleteSizeById(Integer id);
    List<Size> findByStatusActive();
    Page<Size> findAllOrderById(Pageable pageable);
    boolean existsBySizeNumber(Integer sizeNumber);
    boolean existsBySizeNumberAndIdNot(Integer sizeNumber, Integer id);
}
