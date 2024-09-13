package com.example.shopGiay.repository;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM brand WHERE id = ?1")
    Brand getById(Integer id);
    Page<Brand> findByStatusNot(int status, Pageable pageable);
    Page<Brand> findByNameBrandContainingIgnoreCaseAndStatusNot(String name, Integer status, Pageable pageable);
    boolean existsByNameBrand(String nameBrand);
    boolean existsByNameBrandAndIdNot(String nameBrand, Integer id);
    @Query("SELECT b FROM Brand b WHERE b.status = 1")
    List<Brand> findByStatusActive();
}
