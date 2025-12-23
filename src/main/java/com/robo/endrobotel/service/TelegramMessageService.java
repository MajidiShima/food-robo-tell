package com.robo.endrobotel.service;

import com.robo.endrobotel.domain.Food;
import com.robo.endrobotel.domain.Order;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramMessageService {

    private final FoodService foodService;

    public TelegramMessageService(FoodService foodService) {
        this.foodService = foodService;
    }
    public SendMessage buildFoodListMessage(Long chatId) {

        List<Food> foods = foodService.getAllFoods();

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("🍽 لطفاً غذای مورد نظر را انتخاب کنید");

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Food food : foods) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(food.getName());
            button.setCallbackData("FOOD_" + food.getId()); // ✅ اصلاح شد

            rows.add(List.of(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        return message;
    }
    public SendMessage buildConfirmOrderMessage(Long chatId, Order order) {

        String text = """
            📦 خلاصه سفارش:
            
            🍔 غذا: %s
            🔢 تعداد: %d

            آیا سفارش را تایید می‌کنید؟
            """.formatted(
                order.getFood().getName(),
                order.getQuantity()
        );

        InlineKeyboardButton confirmBtn = InlineKeyboardButton.builder()
                .text("✅ تایید")
                .callbackData("CONFIRM_ORDER")
                .build();

        InlineKeyboardButton cancelBtn = InlineKeyboardButton.builder()
                .text("❌ لغو")
                .callbackData("CANCEL_ORDER")
                .build();

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                List.of(List.of(confirmBtn, cancelBtn))
        );

        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboard);

        return message;
    }

}
