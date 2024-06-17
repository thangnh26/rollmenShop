package com.example.shopGiay.service;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();
    Category getCategoryById(Integer id);
    Category saveCategory(Category category);
    void deleteCategoryById(Integer id);

    Page<Category> getAllCategoryPaginated(Pageable pageable);
}

