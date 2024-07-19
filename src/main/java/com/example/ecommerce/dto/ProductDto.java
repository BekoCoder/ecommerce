package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.ProductEnum;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private int price;

    private int quantity;

    private String color;

    private List<ProductEnum> productEnumList;
}
