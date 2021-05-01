package com.diplom.drinksmachine.bot;

import com.diplom.drinksmachine.bot.handlers.*;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@PropertySource("classpath:telegram.properties")
@Slf4j
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private final UserService userService;
    private final InputMessageHandler inputMessageHandler;
    private final InlineQueryHandler inlineQueryHandler;
    private final CurrentDrinkHandler currentDrinkHandler;
    private final CapacityCurrentDrinkHandler capacityCurrentDrinkHandler;
    private final OrderHandler orderHandler;

    public Bot(UserService userService,
               InputMessageHandler inputMessageHandler,
               InlineQueryHandler inlineQueryHandler,
               CurrentDrinkHandler currentDrinkHandler,
               CapacityCurrentDrinkHandler capacityCurrentDrinkHandler,
               OrderHandler orderHandler) {
        this.userService = userService;
        this.inputMessageHandler = inputMessageHandler;
        this.inlineQueryHandler = inlineQueryHandler;
        this.currentDrinkHandler = currentDrinkHandler;
        this.capacityCurrentDrinkHandler = capacityCurrentDrinkHandler;
        this.orderHandler = orderHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {

        User user = getUser(update);

        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();

            if (callbackQuery.startsWith("selectdrink")) {
                sendEditMessageCaption(capacityCurrentDrinkHandler.handler(update, user));
                return;
            }

            if (callbackQuery.startsWith("pay")) {
                sendEditMessageReplyMarkup(orderHandler.handler(update, user));

                log.info("Новый заказ от пользователя: {}, chatID: {}",
                        update.getCallbackQuery().getFrom().getUserName(),
                        update.getCallbackQuery().getMessage().getChatId());
                return;
            }

            if (callbackQuery.startsWith("checkorder")) {
                sendAnswerCallbackQuery(orderHandler.checkOrder(update));
                return;
            }

            if (callbackQuery.startsWith("orderinfo")) {
                sendEditMessageText(orderHandler.orderInfo(update, user));
                return;
            }

            if (callbackQuery.equals("backtoorderlist")) {
                sendEditMessageText(orderHandler.orderList(update, user));
                return;
            }
        }

        if (update.hasInlineQuery()) {
            sendInlineQuery(inlineQueryHandler.handler(update));
        }

        if (update.hasChosenInlineQuery()) {
            sendPhoto(currentDrinkHandler.chosenDrinkMessage(update, user));
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            sendLocation(inputMessageHandler.replyLocation(update, user));
            sendMessage(inputMessageHandler.messageHandler(update, user));

            log.info("Новое сообщение от пользователя {}, chatId: {}, сообщение: {}",
                    update.getMessage().getFrom().getUserName(),
                    update.getMessage().getChatId(),
                    update.getMessage().getText());
        }
    }

    private User getUser(Update update) {
        String userName = null;

        if (update.hasInlineQuery())
            userName = update.getInlineQuery().getFrom().getUserName();
        if (update.hasChosenInlineQuery())
            userName = update.getChosenInlineQuery().getFrom().getUserName();
        if (update.hasMessage())
            userName = update.getMessage().getFrom().getUserName();
        if (update.hasCallbackQuery())
            userName = update.getCallbackQuery().getFrom().getUserName();

        User user = userService.findByUserName(userName);
        if (user == null) {
            if (!update.hasMessage()) return null;
            Long chatId = update.getMessage().getChatId();
            user = new User(chatId, userName);
            userService.addUser(user);
            log.info("Зарегистрирован новый пользователь: {}, chatId: {}", user.getName(), user.getChatId());
        }
        return user;
    }

    private void sendMessage(SendMessage sendMessage) {
        if (sendMessage == null) return;
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendLocation(SendLocation location) {
        if (location == null) return;
        try {
            execute(location);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendInlineQuery(AnswerInlineQuery answerInlineQuery) {
        if (answerInlineQuery == null) return;
        try {
            execute(answerInlineQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendPhoto(SendPhoto sendPhoto) {
        if (sendPhoto == null) return;
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendEditMessageCaption(EditMessageCaption editMessageCaption) {
        if (editMessageCaption == null) return;
        try {
            execute(editMessageCaption);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendEditMessageReplyMarkup(EditMessageReplyMarkup editMessageReplyMarkup) {
        if (editMessageReplyMarkup == null) return;
        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendAnswerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
        if (answerCallbackQuery == null) return;
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendEditMessageText(EditMessageText editMessageText) {
        if (editMessageText == null) return;
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}