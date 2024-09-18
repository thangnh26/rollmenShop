package com.example.shopGiay.service;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    List<Brand> findByStatusActive();
    Brand getBrandById(Integer id);
    Brand saveBrand(Brand brand);
    void deleteBrandById(Integer id);
    Page<Brand> getBrandByStatusNot2(Pageable pageable);
    Page<Brand> searchBrandByName(String keyword, Pageable pageable);
    boolean existsByNameBrand(String nameBrand);
    boolean existsByNameBrandAndIdNot(String nameBrand, Integer id);
}
