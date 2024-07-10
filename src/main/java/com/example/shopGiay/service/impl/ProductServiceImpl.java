
package com.example.shopGiay.service.impl;

import com.example.shopGiay.model.Product;
import com.example.shopGiay.repository.ProductRepository;
import com.example.shopGiay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public List<Product> getListNewProducts(int limit){
        List<Product> products = productRepository.getListNewProducts(limit);
        return products;
    }

    @Override
    public Product getDetailProductById(int id){
        Optional<Product> rs = productRepository.findById(id);
        if(rs.isEmpty()){
            throw new NotFoundException("Sản phẩm không tồn tại");
        }
        Product product = rs.get();
        return product;
    }

    //Tìm kiếm sản phẩm
    @Override
    public Page<Product> searchProduct(String keyword, Pageable pageable){
        if(keyword != null){
            return productRepository.searchProduct(keyword, pageable);
        }else{
            return productRepository.findAll(pageable);
        }
    }

    //phân trang
    @Override
    public Page<Product> findAllOrderById(Pageable pageable){
        Page<Product> pageList = productRepository.findAllOrderById(pageable);
        return pageList;
    }

    @Override
    public List<Product> getRandomListProduct(int limit){
        return productRepository.getRandomListProduct(limit);
    }
}
