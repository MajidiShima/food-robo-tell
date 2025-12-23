package com.robo.endrobotel.controller;

import com.robo.endrobotel.dto.OrderFilter;
import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.repository.OrderRepository;
import com.robo.endrobotel.repository.OrderSpecification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderRepository orderRepository;

    public AdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders")
    public List<Order> getOrders(@RequestBody OrderFilter filter) {
        return orderRepository.findAll(OrderSpecification.filter(filter));
    }
}