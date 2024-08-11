
package com.example.shopGiay.repository;


import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id,p.thumbnail,p.name,p.description,p.status,pd.quantity,pd.price,p.createDate) FROM Product p join ProductDetail pd on p.id = pd.product.id where p.status=1 and pd.status=1 and p.id = :id")
    Optional<ProductDto> getOne(int id);

    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id,p.thumbnail,p.name,p.description,p.status,pd.quantity,pd.price,p.createDate) FROM Product p join ProductDetail pd on p.id = pd.product.id where p.status=1 and pd.status=1 ORDER BY p.createDate DESC")
    List<ProductDto> getListNewProducts(Pageable pageable);

    @Query(value = "SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id,p.thumbnail,p.name,p.description,p.status,pd.quantity,pd.price,p.createDate) FROM Product p join ProductDetail pd on p.id = pd.product.id where p.status=1 and pd.status=1")
    Page<ProductDto> getAll(Pageable pageable);

    //Tìm kiếm sản phẩm
    @Query("SELECT distinct new com.example.shopGiay.dto.ProductDto(p.id,p.thumbnail,p.name,p.description,p.status,pd.quantity,pd.price,p.createDate) FROM Product p join ProductDetail pd on p.id = pd.product.id where p.status=1 and pd.status=1 and p.name LIKE lower(concat('%', :keyword, '%')) or p.brand.nameBrand LIKE lower(concat('%', :keyword, '%')) ")
    Page<ProductDto> searchProduct(String keyword, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    //lấy  sản phẩm
    @Query(nativeQuery = true, value = "SELECT COUNT( p.id)  FROM product p ")
    int countProduct();

    @Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY Id DESC")
    Page<Product> findAllOrderById(Pageable pageable);


//    @Query(value = "SELECT new com.example.shopGiay.dto.ProductDto(p.id,p.thumbnail,p.name,p.description,p.status,pd.quantity,pd.price) FROM product ORDER BY RAND()")
//    List<ProductDto> getRandomListProduct(Pageable pageable);

    @Query(value = "select new com.example.shopGiay.dto.ProductSizeResponse(s.id,s.sizeNumber) from Size s " +
            "where s.id in (select pd.size.id from ProductDetail pd where pd.product.id=:productId)")
    List<ProductSizeResponse> sizeInProductSize(int productId);

    @Query(value = "select distinct new com.example.shopGiay.dto.ProductColorResponse(c.id,c.name) from ProductDetail pd " +
            "join Color c on pd.color.id = c.id " +
            "WHERE pd.product.id = :productId")
    List<ProductColorResponse> colorInProduct(int productId);

    //lấy 4
    @Query("SELECT  new com.example.shopGiay.dto.ProductDto(pd.product.id, pd.product.thumbnail, pd.product.name, pd.product.description, pd.status, pd.quantity, pd.price, pd.createDate) " +
            "FROM ProductDetail pd " +
            "JOIN OrderDetail od ON pd.id = od.productDetail.id " +
            "GROUP BY pd.id " +
            "ORDER BY SUM(od.quantity) DESC")
    List<ProductDto> getProductHot();
}
