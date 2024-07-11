package com.example.ecommerce.entity;

import com.example.ecommerce.entity.enums.ProductEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "products")
public class ProductEntity extends BaseEntity {
    private String name;
    private String description;
    private int price;
    private int quantity;
    private String color;
    @Enumerated(value = EnumType.STRING)
    private List<ProductEnum> productEnumList;

}
