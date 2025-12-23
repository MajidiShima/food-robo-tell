package com.robo.endrobotel.controller;

import com.robo.endrobotel.dto.OrderRequest;
import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.domain.User;
import com.robo.endrobotel.repository.FoodRepository;
import com.robo.endrobotel.repository.OrderRepository;
import com.robo.endrobotel.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserController(FoodRepository foodRepository,
                          UserRepository userRepository,
                          OrderRepository orderRepository) {
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // نمایش فهرست غذاها
    @GetMapping("/foods")
    public List<Food> getFoods() {
        return foodRepository.findAll();
    }

    // ثبت سفارش
    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest request) {

        User user = userRepository.findByTelegramUserId(request.telegramUserId())
                .orElseGet(() -> {
                    User u = new User();
                    u.setTelegramUserId(request.telegramUserId());
                    return userRepository.save(u);
                });

        Food food = foodRepository.findById(request.foodId())
                .orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setFood(food);

        orderRepository.save(order);
    }
}

