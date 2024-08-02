package com.example.ecommerce.entity;

import com.example.ecommerce.entity.enums.ProductEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @JoinColumn(name = "image_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private ImageEntity image;

    @JoinColumn(name = "categories_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private CategoriesEntity categories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @JsonManagedReference
    private List<OrderDetailsEntity> orderDetails;

}
