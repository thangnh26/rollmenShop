
package com.example.shopGiay.service;


import com.example.shopGiay.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {


    //Lấy sản 3 phẩm mới nhất
    List<Product> getListNewProducts(int limit);

    //Lấy danh sách các sản phẩm nổi bật
    List<Product> findAll();

    //Lấy thông tin sản phẩm theo id
    Product getDetailProductById(int id);

    //Lấy danh sách các sản phẩm và tìm kiếm
    Page<Product> searchProduct(String keyword, Pageable pageable);

    Page<Product> findAllOrderById(Pageable pageable);

    //Lấy random danh sách sản phẩm
    List<Product> getRandomListProduct(int limit);
}
