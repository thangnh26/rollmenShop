package com.example.shopGiay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailForm {
    @NotNull(message = "Tên sản phẩm là bắt buộc")
    private String name;

    @NotNull(message = "Thể loại là bắt buộc")
    private Integer categoryId;

    @NotNull(message = "Thương hiệu là bắt buộc")
    private Integer brandId;

    @NotNull(message = "Chất liệu là bắt buộc")
    private Integer materialId;

    @NotNull(message = "Sole là bắt buộc")
    private Integer soleId;

    @NotNull(message = "Kích thước là bắt buộc")
    private List<Integer> sizeIds;

    @NotNull(message = "Màu sắc là bắt buộc")
    private List<Integer> colorIds;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @Min(value = 1, message = "Đơn giá phải lớn hơn 0")
    private Double price;

    private String description;
    private MultipartFile thumbnailUrl;
}



