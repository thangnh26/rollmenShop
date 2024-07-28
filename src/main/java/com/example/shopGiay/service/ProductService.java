
package com.example.shopGiay.service;


import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface ProductService {


    //Lấy sản 3 phẩm mới nhất
    List<ProductDto> getListNewProducts(int limit);

    //Lấy danh sách các sản phẩm nổi bật
    List<Product> findAll();

    //Lấy thông tin sản phẩm theo id
    ProductDto getDetailProductById(int id);

    //Lấy danh sách các sản phẩm và tìm kiếm
    Page<ProductDto> searchProduct(String keyword, Pageable pageable);

    Page<Product> findAllOrderById(Pageable pageable);

    //Lấy random danh sách sản phẩm
//    List<ProductDto> getRandomListProduct(int limit);

    List<ProductSizeResponse> listSize(int productId);

    List<ProductColorResponse> listColor(int productId);
}
