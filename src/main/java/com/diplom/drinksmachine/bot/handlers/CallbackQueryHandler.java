package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Component
public class CallbackQueryHandler {

    private final CapacityCurrentDrinkHandler capacityCurrentDrinkHandler;
    private final OrderHandler orderHandler;
    public CallbackQueryHandler(CapacityCurrentDrinkHandler capacityCurrentDrinkHandler, OrderHandler orderHandler) {
        this.capacityCurrentDrinkHandler = capacityCurrentDrinkHandler;
        this.orderHandler = orderHandler;
    }

    public BotApiMethod<? extends Serializable> handler(Update update, User user) {
        String callbackQuery = update.getCallbackQuery().getData();

        if (callbackQuery.startsWith("selectdrink")) {
            return capacityCurrentDrinkHandler.handler(update, user);
        }

        if (callbackQuery.startsWith("pay")) {
            return orderHandler.paymentMessage(update, user);
        }

        if (callbackQuery.startsWith("orderinfo")) {
            return orderHandler.orderInfo(update, user);
        }

        if (callbackQuery.equals("backtoorderlist")) {
            return orderHandler.orderList(update, user);
        }
        return null;
    }

    public DeleteMessage deleteMessage(Update update, User user) {
        return new DeleteMessage()
                .setChatId(user.getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
    }
}
