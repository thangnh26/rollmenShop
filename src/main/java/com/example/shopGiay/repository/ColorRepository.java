package com.example.shopGiay.repository;

import com.example.shopGiay.model.Color;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Size;
import com.example.shopGiay.model.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    Page<Color> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Color> findByStatusNot(int status, Pageable pageable);

    @Query("SELECT s FROM Color s WHERE s.id = ?1")
    Color getById(int id);

    @Query(value = "SELECT * FROM color WHERE id NOT IN (SELECT color_id FROM product_detail WHERE product_id=:productId)",nativeQuery = true)
    Color findColorNotInProductDetail(Integer productId);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    @Query("SELECT b FROM Color b WHERE b.status = 1")
    List<Color> findByStatusActive();
}
