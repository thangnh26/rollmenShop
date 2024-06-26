package com.example.shopGiay.service;


import com.example.shopGiay.model.ProductDetail;

import java.util.List;

public interface ProductDetailService {
    List<ProductDetail> getAllProductDetails();
    ProductDetail getProductDetailById(Integer id);
    ProductDetail saveProductDetail(ProductDetail productDetail);
    void deleteProductDetail(Integer id);
}
