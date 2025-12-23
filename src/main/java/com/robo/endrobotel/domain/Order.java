package com.robo.endrobotel.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Food food;


    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status ;

    private LocalDateTime createdAt = LocalDateTime.now();
}