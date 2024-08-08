package com.example.shopGiay.repository;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    Page<Color> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Color> findByStatusNot(int status, Pageable pageable);

    @Query("SELECT s FROM Color s WHERE s.id = ?1")
    Color getById(int id);
}
