package com.robo.endrobotel.controller;

import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.dto.OrderFilter;
import com.robo.endrobotel.repository.OrderRepository;
import com.robo.endrobotel.repository.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;

    @PostMapping("/search")
    public List<Order> search(@RequestBody OrderFilter filter) {
        return orderRepository.findAll(OrderSpecification.filter(filter));
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}

