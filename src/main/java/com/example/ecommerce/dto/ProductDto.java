package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.ProductEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Schema(description = "Mahsulot bo'yicha ma'lumotlar")
public class ProductDto {
    @Schema(description = "Mahsulot Id si")
    private Long id;
    @Schema(description = "Mahsulot nomi")
    private String name;
    @Schema(description = "Mahsulot haqida")
    private String description;
    @Schema(description = "Narxi")
    private int price;
    @Schema(description = "Miqdori")
    private int quantity;
    @Schema(description = "Rangi")
    private String color;

    @Schema(description = "Kategoriya Id si")
    private Long categoryId;

    private List<ProductEnum> productEnumList;

    @Schema(description = "Mahsulot rasmi")
    private MultipartFile file;
}
