package com.robo.endrobotel.repository;

import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findFirstByUser_TelegramUserIdAndStatusOrderByCreatedAtDesc(
            Long telegramUserId,
            OrderStatus status
    );

    @Query("""
        SELECT o.status, COUNT(o)
        FROM Order o
        GROUP BY o.status
    """)
    List<Object[]> countByStatus();

}
