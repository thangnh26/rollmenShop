package com.example.shopGiay.repository;

import com.example.shopGiay.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Query(value = "select pd.quantity from ProductDetail pd where pd.id = :proId")
    int checkQuantity(int proId);

    @Query(value = "select pd from ProductDetail pd where pd.product.id = :proId and pd.color.id = :colorId and pd.size.id = :sizeId")
    ProductDetail getOneProductDetail(int proId, int colorId, int sizeId);
}
