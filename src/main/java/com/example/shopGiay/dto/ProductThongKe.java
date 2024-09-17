package com.example.shopGiay.dto;

import lombok.Data;

@Data
public class ProductThongKe {
    private String name;
    private int quantitySold;

    public ProductThongKe(String name, Long quantitySold) {
        this.name = name;
        this.quantitySold = quantitySold != null ? quantitySold.intValue() : 0;
    }
}
