package com.robo.endrobotel.service;

import com.robo.endrobotel.bot.UserSession;
import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.domain.OrderStatus;
import com.robo.endrobotel.domain.User;
import com.robo.endrobotel.repository.FoodRepository;
import com.robo.endrobotel.repository.OrderRepository;

import com.robo.endrobotel.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final UserSession userSession;

    public Order createDraftOrder(Long chatId, int quantity) {

        User user = userRepository.findByTelegramUserId(chatId).get();
        Food food = foodRepository.getReferenceById(userSession.getFood(chatId));

        Order order = Order.builder()
                .user(user)
                .food(food)
                .quantity(quantity)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    public void confirmOrder(Long chatId) {

        Order order = orderRepository
                .findFirstByUser_TelegramUserIdAndStatusOrderByCreatedAtDesc(
                        chatId,
                        OrderStatus.CREATED
                )
                .orElseThrow(() ->
                        new IllegalStateException("No active order to confirm")
                );

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    public void cancelOrder(Long chatId) {

        Order order = orderRepository
                .findFirstByUser_TelegramUserIdAndStatusOrderByCreatedAtDesc(
                        chatId,
                        OrderStatus.CREATED
                )
                .orElseThrow(() ->
                        new IllegalStateException("No active order to cancel")
                );

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }


}
