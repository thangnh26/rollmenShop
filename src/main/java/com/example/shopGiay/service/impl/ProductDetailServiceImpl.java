package com.example.shopGiay.service.impl;


import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.repository.ProductDetailRepository;
import com.example.shopGiay.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }

    @Override
    public ProductDetail getProductDetailById(Integer id) {
        return productDetailRepository.findById(id).orElse(null);
    }

    @Override
    public ProductDetail saveProductDetail(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    @Override
    public void deleteProductDetail(Integer id) {
        productDetailRepository.deleteById(id);
    }
}
