package com.example.shopGiay.dto;

import com.example.shopGiay.model.Brand;
import com.example.shopGiay.model.Category;
import com.example.shopGiay.model.Material;
import com.example.shopGiay.model.Sole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer id;
    private String thumbnail;
    private String name;
    private String description;
    private Integer status;
    private Integer quantity;
    private BigDecimal price;
    private LocalDate createDate;
}
