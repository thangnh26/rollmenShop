package com.example.shopGiay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailForm {
    private Integer categoryId;
    private Integer brandId;
    private Integer materialId;
    private Integer soleId;
    private String name;
    private Integer quantity;
    private Double price;
    private String description;
    private MultipartFile thumbnailUrl;
}



