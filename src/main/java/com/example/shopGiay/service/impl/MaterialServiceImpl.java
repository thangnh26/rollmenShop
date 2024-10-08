package com.example.shopGiay.service.impl;


import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.repository.BrandRepository;
import com.example.shopGiay.repository.MaterialRepository;
import com.example.shopGiay.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MaterialServiceImpl implements MaterialService {


    @Autowired
    MaterialRepository materialRepository;
    @Override
    public Material getMaterialById(Integer id) {
        return materialRepository.findById(id).orElse(null);
    }
    @Override
    public List<Material> getAllMaterial() {
        return materialRepository.findAll();
    }

    @Override
    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Page<Material> getMaterialByStatusNot2(Pageable pageable) {
        return materialRepository.findByStatusNot(2, pageable);
    }

    @Override
    public Page<Material> searchMaterialByName(String keyword, Pageable pageable) {
        return materialRepository.findByNameContainingIgnoreCaseAndStatusNot(keyword, 2, pageable);
    }

}

