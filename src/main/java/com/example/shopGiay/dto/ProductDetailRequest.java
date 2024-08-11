package com.example.shopGiay.dto;

import lombok.Data;

@Data
public class ProductDetailRequest {
    private Integer sizeId;
    private Integer colorId;
    private Integer quantity;
    private Double price;

    // Getters and Setters
}
