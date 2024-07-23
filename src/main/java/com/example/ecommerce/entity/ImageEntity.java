package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private long size;
    private byte [] data;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
