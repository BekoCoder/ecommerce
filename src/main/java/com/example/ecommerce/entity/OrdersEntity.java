package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private Boolean status;

//    @JoinColumn(name = "user_id")
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Long createdBy;

    @CreationTimestamp
    private LocalDateTime createdDate;

}