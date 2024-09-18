package com.example.shopGiay.repository;

import com.example.shopGiay.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("SELECT s FROM Size s WHERE s.id = ?1")
    Size getById(int id);

    @Query(value = "SELECT * FROM size WHERE id NOT IN (SELECT size_id FROM product_detail WHERE product_id=:productId)",nativeQuery = true)
    Size findSizeNotInProductDetail(Integer productId);
    @Query("SELECT b FROM Size b WHERE b.status = 1")
    List<Size> findByStatusActive();
    @Query(nativeQuery = true, value = "SELECT * FROM size ORDER BY Id DESC")
    Page<Size> findAllOrderById(Pageable pageable);
    boolean existsBySizeNumber(Integer sizeNumber);
    boolean existsBySizeNumberAndIdNot(Integer sizeNumber, Integer id);
}
