package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.ProductEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Product bo'yicha ma'lumotlar")
public class ProductDto {

    @Schema(description = "Name")
    private String name;

    @Schema(description = "Mahsulot ta'rifi")
    private String description;

    @Schema(description = "Narxi")
    private int price;

    @Schema(description = "Miqdor")
    private int quantity;

    @Schema(description = "Rangi")
    private String color;

    private List<ProductEnum> productEnumList;
}
