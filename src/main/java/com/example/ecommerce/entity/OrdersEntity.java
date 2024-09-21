package com.example.ecommerce.entity;

import com.example.ecommerce.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Double sum;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonBackReference
    private UserEntity userId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetails = new ArrayList<>();

}
