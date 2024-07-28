package com.example.shopGiay.repository;

import com.example.shopGiay.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Query(value = "select pd.quantity from ProductDetail pd where pd.id = :proId")
    int checkQuantity(int proId);

    @Query(value = "select pd from ProductDetail pd where pd.product.id = :proId and pd.color.id = :colorId and pd.size.id = :sizeId")
    ProductDetail getOneProductDetail(int proId, int colorId, int sizeId);
    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.product_detail WHERE product_id = ?1 AND size_id = ?2")
    ProductDetail findByProductIdAndSizeId(int productId, int sizeId);

    @Query(nativeQuery = true, value ="SELECT * FROM dbshopgiay1.product_detail WHERE product_id = ?1")
    List<ProductDetail> findAllByProductId(int productId);
}
