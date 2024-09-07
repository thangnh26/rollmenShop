package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.repository.BrandRepository;
import com.example.shopGiay.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Integer id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void deleteBrandById(Integer id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Page<Brand> getBrandByStatusNot2(Pageable pageable) {
        return brandRepository.findByStatusNot(2,pageable);
    }

    @Override
    public Page<Brand> searchBrandByName(String keyword, Pageable pageable) {
        return brandRepository.findByNameBrandContainingIgnoreCaseAndStatusNot(keyword,2,pageable);
    }

    @Override
    public boolean existsByNameBrand(String nameBrand) {
        return brandRepository.existsByNameBrand(nameBrand);
    }

    @Override
    public boolean existsByNameBrandAndIdNot(String nameBrand, Integer id) {
        return brandRepository.existsByNameBrandAndIdNot(nameBrand, id);
    }
}
