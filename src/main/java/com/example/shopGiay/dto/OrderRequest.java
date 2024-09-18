package com.example.shopGiay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Integer quantity;
    private Integer sizeId;
    private Integer proId;
    private Integer colorId;

    // Getters and Setters
}

