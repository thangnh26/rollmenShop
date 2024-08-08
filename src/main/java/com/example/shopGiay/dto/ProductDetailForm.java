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
    private List<Integer> categoryId;
    private List<Integer> brandId;
    private List<Integer> materialId;
    private List<Integer> soleId;
    private List<Integer> sizeId;
    private List<Integer> colorId;
    private String name;
    private Integer quantity;
    private Double price;
    private String description;
    private MultipartFile thumbnailUrl;
    // Getters and Setters
}

