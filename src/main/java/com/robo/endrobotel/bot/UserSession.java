package com.robo.endrobotel.bot;

import com.robo.endrobotel.domain.UserState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSession {

    private final Map<Long, UserState> states = new ConcurrentHashMap<>();
    private final Map<Long, Long> selectedFood = new ConcurrentHashMap<>();

    public void setState(Long chatId, UserState state) {
        states.put(chatId, state);
    }

    public UserState getState(Long chatId) {
        return states.getOrDefault(chatId, UserState.NONE);
    }

    public void setFood(Long chatId, Long foodId) {
        selectedFood.put(chatId, foodId);
    }

    public Long getFood(Long chatId) {
        return selectedFood.get(chatId);
    }

    public void clear(Long chatId) {
        states.remove(chatId);
        selectedFood.remove(chatId);
    }
}

