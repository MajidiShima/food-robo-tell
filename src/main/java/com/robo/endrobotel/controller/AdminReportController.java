package com.robo.endrobotel.controller;

import com.robo.endrobotel.domain.OrderStatus;
import com.robo.endrobotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final OrderRepository orderRepository;

    @GetMapping("/order-status")
    public Map<OrderStatus, Long> orderStatusReport() {

        Map<OrderStatus, Long> result = new HashMap<>();

        for (Object[] row : orderRepository.countByStatus()) {
            result.put((OrderStatus) row[0], (Long) row[1]);
        }
        return result;
    }
}
