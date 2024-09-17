
package com.example.shopGiay.service.impl;

import com.example.shopGiay.dto.ProductColorResponse;
import com.example.shopGiay.dto.ProductDto;
import com.example.shopGiay.dto.ProductSizeResponse;
import com.example.shopGiay.dto.ProductThongKe;
import com.example.shopGiay.model.Order;
import com.example.shopGiay.model.OrderDetail;
import com.example.shopGiay.model.Product;
import com.example.shopGiay.model.ProductDetail;
import com.example.shopGiay.repository.OrderRepository;
import com.example.shopGiay.repository.ProductDetailRepository;
import com.example.shopGiay.repository.ProductRepository;
import com.example.shopGiay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public List<Product> findAll() {
        List<Product> listProduct = productRepository.findAll();
        return listProduct;
    }

    @Override
    public List<ProductDto> getListNewProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ProductDto> products = productRepository.getProduct(pageable);
        return products;
    }

    @Override
    public List<ProductDto> getNewProducts8(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ProductDto> products8moi = productRepository.getNewProducts8(pageable);
        return products8moi;
    }

    @Override
    public Product getDetailProductById(int id) {
        return productRepository.getOne(id).orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại"));
    }

    @Override
    public List<ProductDetail> getListDetailProductById(int id) {
        return productDetailRepository.findByProductId(id);
    }

    //Tìm kiếm sản phẩm
    @Override
    public Page<ProductDto> searchProduct(String keyword, Pageable pageable) {
        if (keyword != null) {
            return productRepository.searchProduct(keyword, pageable);
        } else {
            return productRepository.getAllDto(pageable);
        }
    }

    //phân trang
    @Override
    public Page<Product> findAllOrderById(Pageable pageable) {
        Page<Product> pageList = productRepository.findAllOrderById(pageable);
        return pageList;
    }

    @Override
    public List<ProductSizeResponse> listSize(int productId) {
        return productRepository.sizeInProductSize(productId);
    }

    @Override
    public List<ProductSizeResponse> listSizeByList(List<Integer> productDetailIds) {
        List<ProductSizeResponse> sizeResponses = new ArrayList<>();

        // Assuming there's a method in your repository to fetch product details by their IDs
        List<ProductDetail> productDetails = productDetailRepository.findAllByProduct(productDetailIds);
        return sizeResponses;
    }


    @Override
    public List<ProductColorResponse> listColor(int productId) {
        return productRepository.colorInProduct(productId);
    }

    @Override
    public List<ProductColorResponse> listColorByList(List<Integer> productDetailIds) {
        List<ProductColorResponse> sizeResponses = new ArrayList<>();

        // Assuming there's a method in your repository to fetch product details by their IDs
        List<ProductDetail> productDetails = productDetailRepository.findAllByProduct(productDetailIds);

        for (ProductDetail productDetail : productDetails) {
            ProductColorResponse response = new ProductColorResponse();
            response.setNameColor(productDetail.getColor().getName());
            response.setId(productDetail.getColor().getId());
            sizeResponses.add(response);
        }

        return sizeResponses;
    }

    @Override
    public List<Product> getProductHot() {

        return productRepository.getProductHot();
    }

    @Override
    public List<ProductThongKe> getProductHotthongke() {
        return productRepository.getProductHotthongke();
    }

    @Override
    public void updateProductDetails(Integer id, BigDecimal price, Integer quantity) {
        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tồn tại"));
        productDetail.setPrice(price);
        productDetail.setQuantity(quantity);
        productDetailRepository.save(productDetail);
    }
    @Override
    public void saveOrUpdateProduct(Product product) {
        productRepository.save(product);
    }
}
