package com.example.shopGiay.repository;

import com.example.shopGiay.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByStatusNot(int status, Pageable pageable);
    Page<Category> findByNameCategoryContainingIgnoreCaseAndStatusNot(String name, Integer status, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM category WHERE id = ?1")
    Category getById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM category ORDER BY id DESC ")
    List<Category> findAll();

    @Query(nativeQuery = true, value = "SELECT name_category FROM category WHERE brand_id = ?1")
    List<String> getListCategoryOfBrand(int id);
    boolean existsByNameCategory(String nameCategory);
    boolean existsByNameCategoryAndIdNot(String nameCategory, Integer id);
}
