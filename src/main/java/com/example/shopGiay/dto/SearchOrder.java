package com.example.shopGiay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchOrder {
    private String codeCustomer;
    private Integer status;
    private String phone;
}
