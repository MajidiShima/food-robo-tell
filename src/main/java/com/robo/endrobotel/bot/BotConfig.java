package com.robo.endrobotel.bot;

import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.repository.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    private final FoodRepository foodRepository;

    public BotConfig(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Bean
    public TelegramLongPollingBot telegramBot(FoodBot foodBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            foodBot.clearWebhook();
        } catch (Exception e) {
            System.out.println("Webhook was already cleared or error ignored: " + e.getMessage());
        }

        botsApi.registerBot(foodBot);
        return foodBot;
    }

    @Bean
    public CommandLineRunner demoRunner() {
        return args -> {
            foodRepository.save(new Food(1001L, "P1","Pizza"));
            foodRepository.save(new Food(1002L, "ch","cholo khoresht"));
            foodRepository.save(new Food(1003L, "so","spkhari"));
            foodRepository.save(new Food(1004L, "de","salad"));

        };
    }

}


