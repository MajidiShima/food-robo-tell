package com.robo.endrobotel.dto;

import com.robo.endrobotel.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFilter {
    private OrderStatus status;
    private Long telegramUserId;
    private LocalDateTime from;
    private LocalDateTime to;
}

