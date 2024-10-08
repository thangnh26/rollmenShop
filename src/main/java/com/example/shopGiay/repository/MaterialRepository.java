package com.example.shopGiay.repository;

import com.example.shopGiay.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Page<Material> findByStatusNot(int status, Pageable pageable);
    Page<Material> findByNameContainingIgnoreCaseAndStatusNot(String name, Integer status, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.material WHERE id = ?1")
    Material getById(Integer id);
}
