package com.robo.endrobotel.bot;

import com.robo.endrobotel.domain.Order;
import com.robo.endrobotel.domain.UserState;
import com.robo.endrobotel.service.OrderService;
import com.robo.endrobotel.service.TelegramMessageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class FoodBot extends TelegramLongPollingBot {


    @PostConstruct
    public void init() {
        System.out.println("BOT TOKEN = " + token);
        System.out.println("username = " + username);
    }

    private final TelegramMessageService messageService;
    private final OrderService orderService;
    private final UserSession userSession;


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    public FoodBot(TelegramMessageService messageService,
                   OrderService orderService,UserSession userSession) {
        this.messageService = messageService;
        this.orderService = orderService;
        this.userSession= userSession;
    }


    @Override
    public String getBotUsername() {
        System.out.println("***********- getbotusername -********");
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            System.out.println("**********  update RECEIVED" + update);
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            // /start
            if ("/start".equals(text)) {
                userSession.setState(chatId, UserState.SELECTING_FOOD);
                executeSafely(messageService.buildFoodListMessage(chatId));
                return;
            }

            // 🔢 ENTER QUANTITY
            if (userSession.getState(chatId) == UserState.ENTERING_QUANTITY) {

                int quantity=Integer.parseInt(text);
                try {
                    if(!text.matches("\\d+")){
                        executeSafely(new SendMessage(
                                chatId.toString(),
                                "❌ لطفاً فقط عدد وارد کنید"));
                    }

                } catch (NumberFormatException e) {
                    executeSafely(new SendMessage(
                            chatId.toString(),
                            "!!!!!!! لطفاً فقط عدد وارد کنید"
                    ));
                    return;
                }

                if (quantity <= 0) {
                    executeSafely(new SendMessage(
                            chatId.toString(),
                            "❌ تعداد باید بزرگتر از صفر باشد"
                    ));
                    return;
                }

                Order order = orderService.createDraftOrder(chatId, quantity);

                userSession.setState(chatId, UserState.CONFIRMING_ORDER);

                executeSafely(
                        messageService.buildConfirmOrderMessage(chatId, order)
                );
            }
        }

        /* ===============================
            CALLBACK HANDLING
           =============================== */
        if (update.hasCallbackQuery()) {

            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            // 🍔 FOOD SELECTION
            if (data.startsWith("FOOD_")) {

                Long foodId = Long.parseLong(data.replace("FOOD_", ""));
                userSession.setFood(chatId, foodId);
                userSession.setState(chatId, UserState.ENTERING_QUANTITY);

                executeSafely(new SendMessage(
                        chatId.toString(),
                        " تعداد را وارد کنید:"
                ));
                return;
            }

            //  CONFIRM ORDER
            if ("CONFIRM_ORDER".equals(data)) {

                orderService.confirmOrder(chatId);
                userSession.clear(chatId);

                executeSafely(new SendMessage(
                        chatId.toString(),
                        " سفارش شما با موفقیت ثبت شد"
                ));
                return;
            }

            //  CANCEL ORDER
            if ("CANCEL_ORDER".equals(data)) {

                orderService.cancelOrder(chatId);
                userSession.clear(chatId);

                executeSafely(new SendMessage(
                        chatId.toString(),
                        "❌ سفارش لغو شد"
                ));
            }
        }
    }


    private void executeSafely (SendMessage message){
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

