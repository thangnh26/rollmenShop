
package com.example.shopGiay.service.impl;

import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.repository.ProductRepository;
import com.example.shopGiay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> findAll(){
        List<Product> listProduct = productRepository.findAll();
        return listProduct;
    }

    @Override
    public List<ProductDto> getListNewProducts(int limit){
        Pageable pageable = PageRequest.of(0,limit);
        List<ProductDto> products = productRepository.getListNewProducts(pageable);
        return products;
    }

    @Override
    public ProductDto getDetailProductById(int id){
        return productRepository.getOne(id).orElseThrow(()-> new NotFoundException("Sản phẩm không tồn tại"));
    }

    //Tìm kiếm sản phẩm
    @Override
    public Page<ProductDto> searchProduct(String keyword, Pageable pageable){
        if(keyword != null){
            return productRepository.searchProduct(keyword, pageable);
        }else{
            return productRepository.getAll(pageable);
        }
    }

    //phân trang
    @Override
    public Page<Product> findAllOrderById(Pageable pageable){
        Page<Product> pageList = productRepository.findAllOrderById(pageable);
        return pageList;
    }

//    @Override
//    public List<ProductDto> getRandomListProduct(int limit){
//        Pageable pageable = PageRequest.of(0,limit);
//        return productRepository.getRandomListProduct(pageable);
//    }

    @Override
    public List<ProductSizeResponse> listSize(int productId) {
        return productRepository.sizeInProductSize(productId);
    }

    @Override
    public List<ProductColorResponse> listColor(int productId) {
        return productRepository.colorInProduct(productId);
    }

    @Override
    public List<ProductDto> getProductHot() {
        return productRepository.getProductHot();
    }


}
