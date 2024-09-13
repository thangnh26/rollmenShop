package com.example.shopGiay.repository;

import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoleRepository extends JpaRepository<Sole, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM sole WHERE id = ?1")
    Sole getById(Integer id);
    Page<Sole> findByStatusNot(int status, Pageable pageable);
    Page<Sole> findByNameContainingIgnoreCaseAndStatusNot(String name, Integer status, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    @Query("SELECT b FROM Sole b WHERE b.status = 1")
    List<Sole> findByStatusActive();
}