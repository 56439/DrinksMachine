package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.CafeService;
import com.diplom.drinksmachine.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class InputMessageHandler {

    private final CafeService cafeService;
    private final UserService userService;
    private final OrderHandler orderHandler;
    public InputMessageHandler(CafeService cafeService, UserService userService, OrderHandler orderHandler) {
        this.cafeService = cafeService;
        this.userService = userService;
        this.orderHandler = orderHandler;
    }

    public BotApiMethod<? extends Serializable> messageHandler(Update update, User user) {
        Message message = update.getMessage();
        String messageText = message.getText();

        switch (messageText) {
            case "/start":
            case "◀ Назад":
                return replyMessage(user, "Главное меню", mainMenuKeyboard(user));
            case "☕ Выбрать кофейню":
                return replyMessage(user, "☕ Кофейни", cafeListKeyboard());
            case "\uD83D\uDCC3 Мои заказы":
                return orderList(user, orderHandler.orderListKeyboard(user));
            case "➕ Сделать заказ":
                return getSendLocation(user, message);
            default:
                return replyLocation(update, user);
        }
    }

    private InlineKeyboardMarkup currentCafeMenuKeyboard() {

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton()
                .setText("\uD83C\uDF7D Меню")
                .setSwitchInlineQueryCurrentChat("Меню"));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    private ReplyKeyboardMarkup cafeListKeyboard() {

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        Iterable<Cafe> cafes = cafeService.findAllCafe();

        KeyboardRow keyboardRow;

        for (Cafe cafe : cafes) {
            keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton().setText(cafe.getAddress()));
            keyboardRowList.add(keyboardRow);
        }
        keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton().setText("◀ Назад"));
        keyboardRowList.add(keyboardRow);

        return new ReplyKeyboardMarkup()
                .setOneTimeKeyboard(false)
                .setResizeKeyboard(true)
                .setKeyboard(keyboardRowList);
    }

    private ReplyKeyboardMarkup mainMenuKeyboard(User user) {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        if (user.getCafe() != null) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton().setText("➕ Сделать заказ"));
            keyboardRow.add(new KeyboardButton().setText("\uD83D\uDCC3 Мои заказы"));
            keyboardRowList.add(keyboardRow);
        }

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton().setText("☕ Выбрать кофейню"));
        keyboardRowList.add(keyboardRow);

        return new ReplyKeyboardMarkup()
                .setOneTimeKeyboard(false)
                .setResizeKeyboard(true)
                .setKeyboard(keyboardRowList);
    }

    private SendLocation getSendLocation(User user, Message message) {
        String[] parts = user.getCafe().getLocation().split(",");
        float latitude = Float.parseFloat(parts[0]);
        float longitude = Float.parseFloat(parts[1]);

        return new SendLocation()
                .setChatId(message.getChatId())
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setReplyMarkup(currentCafeMenuKeyboard())
                .setReplyToMessageId(message.getMessageId());
    }

    public BotApiMethod<? extends Serializable> replyLocation(Update update, User user) {
        Message message = update.getMessage();
        String messageText = message.getText();

        Cafe cafe = cafeService.findByAddress(messageText);

        if (cafe != null) {
            user.setCafe(cafe);
            userService.updateUser(user);

            return getSendLocation(user, message);
        }

        return null;
    }

    private SendMessage replyMessage(User user, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        return new SendMessage()
                .setText(text)
                .setChatId(user.getChatId())
                .setReplyMarkup(replyKeyboardMarkup);
    }

    private SendMessage orderList(User user, InlineKeyboardMarkup inlineKeyboardMarkup) {
        StringBuilder messageText = orderHandler.getOrderList(user);

        return new SendMessage()
                .setParseMode("HTML")
                .setText(String.valueOf(messageText))
                .setChatId(user.getChatId())
                .setReplyMarkup(inlineKeyboardMarkup);
    }
}
