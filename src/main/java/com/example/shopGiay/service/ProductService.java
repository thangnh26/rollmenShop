
package com.example.shopGiay.service;


import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

//@Service
public interface ProductService {


    //Lấy sản 10 phẩm mới nhất
    List<Product> getListNewProducts(int limit);

    //Lấy danh sách các sản phẩm nổi bật
    List<Product> findAll();

    //Lấy thông tin sản phẩm theo id
    Product getDetailProductById(int id);
    List<ProductDetail> getListDetailProductById(int id);

    //Lấy danh sách các sản phẩm và tìm kiếm
    Page<Product> searchProduct(String keyword, Pageable pageable);

    Page<Product> findAllOrderById(Pageable pageable);

    //Lấy random danh sách sản phẩm
//    List<ProductDto> getRandomListProduct(int limit);

    List<ProductSizeResponse> listSize(int productId);
    List<ProductSizeResponse> listSizeByList(List<Integer> productDetailIds);

    List<ProductColorResponse> listColor(int productId);
    List<ProductColorResponse> listColorByList(List<Integer> productDetailIds);

    List<Product> getProductHot();

    void updateProductDetails(Integer id, BigDecimal price, Integer quantity);
}
