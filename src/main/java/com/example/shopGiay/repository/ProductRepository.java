
package com.example.shopGiay.repository;


import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.dto.ProductThongKe;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT distinct p FROM Product p WHERE p.id = :id")
    Optional<Product> getOne(int id);

    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id, p.thumbnail, p.name, (SELECT CAST(MIN(pd.price) AS java.math.BigDecimal) FROM ProductDetail pd WHERE pd.product.id = p.id)) FROM Product p  where p.status=1 ORDER BY p.createDate DESC")
    List<ProductDto> getListNewProducts(Pageable pageable);

    //Tìm kiếm sản phẩm
    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id, p.thumbnail, p.name, " +
            "(SELECT CAST(MIN(pd.price) AS java.math.BigDecimal) FROM ProductDetail pd WHERE pd.product.id = p.id)) " +
            "FROM Product p " +
            "WHERE p.status=1 AND (lower(p.name) LIKE lower(concat('%', :keyword, '%')) OR lower(p.brand.nameBrand) LIKE lower(concat('%', :keyword, '%'))) ",
            countQuery = "SELECT COUNT(p) FROM Product p " +
                    "WHERE p.status=1 AND (lower(p.name) LIKE lower(concat('%', :keyword, '%')) OR lower(p.brand.nameBrand) LIKE lower(concat('%', :keyword, '%')))")
    Page<ProductDto> searchProduct(String keyword, Pageable pageable);

    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id, p.thumbnail, p.name, " +
            "(SELECT CAST(MIN(pd.price) AS java.math.BigDecimal) FROM ProductDetail pd WHERE pd.product.id = p.id)) " +
            "FROM Product p WHERE p.status=1 ORDER BY p.createDate DESC",
            countQuery = "SELECT COUNT(p) FROM Product p WHERE p.status=1")
    Page<ProductDto> getAllDto(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY Id DESC")
    Page<Product> findAllOrderById(Pageable pageable);


    @Query(value = "select new com.example.shopGiay.dto.ProductSizeResponse(s.id,s.sizeNumber) from Size s " +
            "where s.id in (select pd.size.id from ProductDetail pd where pd.product.id=:productId)")
    List<ProductSizeResponse> sizeInProductSize(int productId);

    @Query(value = "select distinct new com.example.shopGiay.dto.ProductColorResponse(c.id,c.name) from ProductDetail pd " +
            "join Color c on pd.color.id = c.id " +
            "WHERE pd.product.id = :productId")
    List<ProductColorResponse> colorInProduct(int productId);

    //san pham moi nhat
    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id, p.thumbnail, p.name, (SELECT CAST(MIN(pd.price) AS java.math.BigDecimal) FROM ProductDetail pd WHERE pd.product.id = p.id)) FROM Product p  where p.status=1 ORDER BY p.id DESC")
    List<ProductDto> getNewProducts8(Pageable pageable);

    //san pham noi bat
    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id, p.thumbnail, p.name, (SELECT CAST(MIN(pd.price) AS java.math.BigDecimal) FROM ProductDetail pd WHERE pd.product.id = p.id)) FROM Product p where p.status=1 and p.brand.nameBrand ='Adidas' ")
    List<ProductDto> getProduct(Pageable pageable);

    //lấy 4
    @Query(value = "SELECT * " +
            "FROM product p " +
            "JOIN product_detail pd ON p.id = pd.product_id " +
            "JOIN order_detail od ON pd.id = od.product_detail_id join dbshopgiay.order o on o.id=od.order_id where o.status=3 " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(od.quantity) DESC " +
            "LIMIT 4 ",nativeQuery = true)
    List<Product> getProductHot();

    // lấy ra list 10 sản phẩm bán chạy nhất bên thôngs kê
    @Query(value = "SELECT new com.example.shopGiay.dto.ProductThongKe(p.name, SUM(od.quantity)) " +
            "FROM Product p " +
            "JOIN ProductDetail pd ON p.id = pd.product.id " +
            "JOIN OrderDetail od ON pd.id = od.productDetail.id " +
            "JOIN Order o ON o.id = od.order.id " +
            "WHERE o.status = 3 " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<ProductThongKe> getProductHotthongke();
    List<Product> findByName(String name);
    Optional<Product> findByNameAndIdNot(String name, Integer id);
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.name = :name AND p.id <> :id")
    boolean existsByNameAndIdNot(String name, Integer id);

}
