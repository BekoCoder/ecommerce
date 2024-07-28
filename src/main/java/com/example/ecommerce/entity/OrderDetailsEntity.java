package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JoinColumn(name = "product_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ProductEntity product;
//
//    @JoinColumn(name = "order_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private OrdersEntity orders;

    private Double quantity;

    @CreationTimestamp
    private LocalDateTime createdDate;
}