package com.example.shopGiay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCart {
    private Integer cartId;
    private Integer quantity;
    private Integer sizeId;
    private Integer proId;
    private Integer colorId;
}
