package com.diplom.drinksmachine.bot;

import com.diplom.drinksmachine.bot.handlers.*;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

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
    private final OrderHandler orderHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    public Bot(UserService userService,
               InputMessageHandler inputMessageHandler,
               InlineQueryHandler inlineQueryHandler,
               CurrentDrinkHandler currentDrinkHandler,
               OrderHandler orderHandler, CallbackQueryHandler callbackQueryHandler) {
        this.userService = userService;
        this.inputMessageHandler = inputMessageHandler;
        this.inlineQueryHandler = inlineQueryHandler;
        this.currentDrinkHandler = currentDrinkHandler;
        this.orderHandler = orderHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {

        User user = getUser(update);

        if (update.hasPreCheckoutQuery()) {
            sendAnswerPreCheckoutQuery(orderHandler.checkoutPayment(update));
            return;
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith("pay")) {
                sendAnswer(callbackQueryHandler.deleteMessage(update, user));
            }
            if (update.getCallbackQuery().getData().startsWith("wait")) {
                String callback = update.getCallbackQuery().getData();
                sendAnswer(callbackQueryHandler.timeInfo(update, callback));
            }
            sendAnswer(callbackQueryHandler.handler(update, user));
            return;
        }

        if (update.hasInlineQuery()) {
            sendInlineQuery(inlineQueryHandler.handler(update));
            return;
        }

        if (update.hasChosenInlineQuery()) {
            sendPhoto(currentDrinkHandler.chosenDrinkMessage(update, user));
            return;
        }

        if (update.hasMessage()) {
            if (update.getMessage().hasSuccessfulPayment()) {
                String payload = update.getMessage().getSuccessfulPayment().getInvoicePayload();
                orderHandler.addOrder(payload, user);

                log.info("Новый заказ от пользователя: {}, chatID: {}, menuId: {}",
                        update.getMessage().getFrom().getUserName(),
                        update.getMessage().getChatId(),
                        update.getMessage().getSuccessfulPayment().getInvoicePayload());
                return;
            }

            if (update.getMessage().hasText()) {
                sendAnswer(inputMessageHandler.messageHandler(update, user));

                log.info("Новое сообщение от пользователя {}, chatId: {}, сообщение: {}",
                        update.getMessage().getFrom().getUserName(),
                        update.getMessage().getChatId(),
                        update.getMessage().getText());
            }
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
        if (update.hasPreCheckoutQuery())
            userName = update.getPreCheckoutQuery().getFrom().getUserName();

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
    private void sendAnswerPreCheckoutQuery(AnswerPreCheckoutQuery answerPreCheckoutQuery) {
        try {
            execute(answerPreCheckoutQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswer(BotApiMethod<? extends Serializable> answer) {
        if (answer == null) return;
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}