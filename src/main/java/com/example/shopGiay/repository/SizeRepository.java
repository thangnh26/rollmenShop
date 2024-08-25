package com.example.shopGiay.repository;

import com.example.shopGiay.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("SELECT s FROM Size s WHERE s.id = ?1")
    Size getById(int id);

    @Query(value = "SELECT * FROM size WHERE id NOT IN (SELECT size_id FROM product_detail WHERE product_id=:productId)",nativeQuery = true)
    Size findSizeNotInProductDetail(Integer productId);
}
