package com.example.shopGiay.service;

import com.example.shopGiay.model.Size;
import com.example.shopGiay.model.Sole;

import java.util.List;

public interface SizeService {
    List<Size> getAllSizes();
    Size getSizeById(Integer id);
    Size saveSize(Size size);
    void deleteSizeById(Integer id);
    List<Size> findByStatusActive();
}
