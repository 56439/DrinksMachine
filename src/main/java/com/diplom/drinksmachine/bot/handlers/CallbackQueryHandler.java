package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Component
public class CallbackQueryHandler {

    private final CapacityCurrentDrinkHandler capacityCurrentDrinkHandler;
    private final CurrentDrinkHandler currentDrinkHandler;
    private final OrderHandler orderHandler;
    private final SelectTimeOfOrder selectTimeOfOrder;
    public CallbackQueryHandler(CapacityCurrentDrinkHandler capacityCurrentDrinkHandler,
                                CurrentDrinkHandler currentDrinkHandler,
                                OrderHandler orderHandler,
                                SelectTimeOfOrder selectTimeOfOrder) {
        this.capacityCurrentDrinkHandler = capacityCurrentDrinkHandler;
        this.currentDrinkHandler = currentDrinkHandler;
        this.orderHandler = orderHandler;
        this.selectTimeOfOrder = selectTimeOfOrder;
    }

    public BotApiMethod<? extends Serializable> handler(Update update, User user) {
        String callbackQuery = update.getCallbackQuery().getData();

        if (callbackQuery.startsWith("wait")) {
            return selectTimeOfOrder.handler(update, user);
        }

        if (callbackQuery.startsWith("confirm")) {
            return currentDrinkHandler.timeKeyboard(update, user);
        }

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

    public AnswerCallbackQuery timeInfo(Update update, String callback) {
        callback = callback.substring(4);
        String text = "Бариста увидит заказ ";
        if (callback.equals("0")) {
            text += "немедленно.";
        } else {
            text += "через " + callback + " минут.";
        }
        return new AnswerCallbackQuery()
                .setShowAlert(true)
                .setCallbackQueryId(update.getCallbackQuery().getId())
                .setText(text);
    }
}
