package com.example.shopGiay.repository;

import com.example.shopGiay.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM brand WHERE id = ?1")
    Brand getById(Integer id);
}
