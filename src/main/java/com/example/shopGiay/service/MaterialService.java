package com.example.shopGiay.service;


import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {

    List<Material> getAllMaterial();
    Material getMaterialById(Integer id);
    Material saveMaterial(Material material);
    void deleteMaterialById(Integer id);

    Page<Material> getAllMaterialsPaginated(Pageable pageable);
}