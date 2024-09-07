package com.example.shopGiay.repository;

import com.example.shopGiay.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Query(value = "select pd.quantity from ProductDetail pd where pd.id = :proId")
    int checkQuantity(int proId);

    @Query(value = "select pd from ProductDetail pd where pd.product.id = :proId and pd.color.id = :colorId and pd.size.id = :sizeId")
    ProductDetail getOneProductDetail(int proId, int colorId, int sizeId);

    @Query(value = "select pd from ProductDetail pd where pd.product.id = :proId and pd.color.id = :colorId and pd.size.id = :sizeId and pd.status=1")
    ProductDetail getOne(int proId, int colorId, int sizeId);

    @Query(nativeQuery = true, value = "SELECT * FROM dbshopgiay1.product_detail WHERE product_id = ?1 AND size_id = ?2")
    ProductDetail findByProductIdAndSizeId(int productId, int sizeId);

    @Query(nativeQuery = true, value ="SELECT distinct * FROM dbshopgiay.product_detail WHERE product_id = ?1")
    List<ProductDetail> findByProductId(int productId);

    @Query(value = "SELECT DISTINCT pd.id, pd.* " +
            "FROM product_detail pd " +
            "JOIN product p ON pd.product_id = p.id " +
            "WHERE p.id IN(:productIds)",nativeQuery = true)
    List<ProductDetail> findAllByProduct(List<Integer> productIds);

    @Query(value = "select min(p.price) from ProductDetail p where p.product.id in :ids group by p.product.id, p.price")
    List<BigDecimal> findListPricreByProductId(List<Integer> ids);

    @Query(value = "select * from product_detail where product_id=:id and status=1 limit 1",nativeQuery = true)
    ProductDetail findPricreByProductId(Integer id);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.size.id = :sizeId AND pd.color.id = :colorId AND pd.product.id = :productId")
    ProductDetail findBySizeIdAndColorIdAndProductId(@Param("sizeId") Integer sizeId,
                                                     @Param("colorId") Integer colorId,
                                                     @Param("productId") Integer productId);

    @Query(value = "select p.quantity from ProductDetail p where p.product.id in :ids group by p.product.id, p.quantity")
    List<Integer> findListQuantityByProductId(Stream<Integer> ids);
}
