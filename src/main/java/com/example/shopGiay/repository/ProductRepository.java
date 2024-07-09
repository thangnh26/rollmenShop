
package com.example.shopGiay.repository;


import com.example.shopGiay.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.product WHERE status = 1 ORDER BY created_at DESC limit ?1")
    List<Product> getListNewProducts(int limit);

    //Tìm kiếm sản phẩm
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    Page<Product> searchProduct(String keyword,Pageable pageable);

    Page<Product> findAll(Pageable pageable);
    //lấy  sản phẩm
    @Query(nativeQuery = true, value = "SELECT COUNT( p.id)  FROM product p ")
    int countProduct();

    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.product ORDER BY Id DESC")
    Page<Product> findAllOrderById(Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.product ORDER BY RAND() LIMIT ?1")
    List<Product> getRandomListProduct(int limit);

}
