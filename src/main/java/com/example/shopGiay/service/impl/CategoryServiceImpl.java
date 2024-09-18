package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.repository.CategoryRepository;
import com.example.shopGiay.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<Category> getAllCategoryPaginated(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Page<Category> getCategoriesByStatusNot2(Pageable pageable) {
        return categoryRepository.findByStatusNot(2, pageable);
    }

    public Page<Category> searchCategoriesByName(String keyword, Pageable pageable) {
        return categoryRepository.findByNameCategoryContainingIgnoreCaseAndStatusNot(keyword,2, pageable);
    }
    @Override
    public boolean existsByNameCategory(String nameCategory) {
        return categoryRepository.existsByNameCategory(nameCategory);
    }

    @Override
    public boolean existsByNameCategoryAndIdNot(String nameCategory, Integer id) {
        return categoryRepository.existsByNameCategoryAndIdNot(nameCategory, id);
    }

    @Override
    public List<Category> findByStatusActive() {
        return categoryRepository.findByStatusActive();
    }


}
