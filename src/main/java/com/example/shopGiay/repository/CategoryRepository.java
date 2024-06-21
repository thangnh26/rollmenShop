package com.example.shopGiay.repository;

import com.example.shopGiay.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByStatusNot(int status, Pageable pageable);
    Page<Category> findByNameCategoryContainingIgnoreCaseAndStatusNot(String name, Integer status, Pageable pageable);

}
