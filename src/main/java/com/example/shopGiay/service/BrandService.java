package com.example.shopGiay.service;

import com.example.shopGiay.model.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand getBrandById(Integer id);
    Brand saveBrand(Brand brand);
    void deleteBrandById(Integer id);
}
