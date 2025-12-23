package com.robo.endrobotel.service;

import com.robo.endrobotel.domain.User;
import com.robo.endrobotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getOrCreate(Long chatId) {
        return userRepository.findByTelegramUserId(chatId)
                .orElseGet(() -> {
                    User user = new User();
                    user.setTelegramUserId(chatId);
                    return userRepository.save(user);
                });
    }
}
