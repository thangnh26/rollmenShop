package com.example.shopGiay.service;


import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {
    List<Material> getAllMaterial();
    Material getMaterialById(Integer id);
    Material saveMaterial(Material material);
    Page<Material> getMaterialByStatusNot2(Pageable pageable);
    Page<Material> searchMaterialByName(String keyword, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    List<Material> findByStatusActive();
}