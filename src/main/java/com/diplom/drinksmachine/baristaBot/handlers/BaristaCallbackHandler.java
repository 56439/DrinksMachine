package com.diplom.drinksmachine.baristaBot.handlers;

import com.diplom.drinksmachine.baristaBot.BaristaBot;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BaristaCallbackHandler {

    private final OrderService orderService;
    private final OrderMessageFromBarista orderMessageFromBarista;
    public BaristaCallbackHandler(OrderService orderService, OrderMessageFromBarista orderMessageFromBarista) {
        this.orderService = orderService;
        this.orderMessageFromBarista = orderMessageFromBarista;
    }

    public void handler(Update update, Barista barista, BaristaBot bot) {
        String callback = update.getCallbackQuery().getData();
        Message message = update.getCallbackQuery().getMessage();

        if (callback.startsWith("ready")) {
            long orderId = Long.parseLong(callback.substring(5));
            Order order = orderService.findById(orderId);
            order.setReady(true);
            orderService.updateOrder(order);
            orderMessageFromBarista.readyOrderRequest(order);

            EditMessageReplyMarkup replyMarkup = setIssuedButton(barista, order.getId(), message.getMessageId());
            sendEditMessageKeyboard(replyMarkup, bot);

            log.info("Бариста {} {} {} приготовил заказ №{}",
                    barista.getSecondName(), barista.getFirstName(), barista.getThirdName(), order.getId());
        }

        if (callback.startsWith("issued")) {
            long orderId = Long.parseLong(callback.substring(6));
            Order order = orderService.findById(orderId);
            order.setIssued(true);
            orderService.updateOrder(order);
            orderMessageFromBarista.issuedOrderRequest(order);

            EditMessageReplyMarkup replyMarkup = deleteInlineKeyboard(barista, message.getMessageId());
            sendEditMessageKeyboard(replyMarkup, bot);

            log.info("Бариста {} {} {} выдал заказ №{}",
                    barista.getSecondName(), barista.getFirstName(), barista.getThirdName(), order.getId());
        }
    }

    private EditMessageReplyMarkup setIssuedButton(Barista barista, Long orderId, Integer messageId) {
        return new EditMessageReplyMarkup()
                .setChatId(barista.getChatId())
                .setMessageId(messageId)
                .setReplyMarkup(inlineIssuedButton(orderId));
    }

    private InlineKeyboardMarkup inlineIssuedButton(long orderId) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton()
                .setText("\uD83D\uDCE6 Выдан")
                .setCallbackData("issued" + orderId));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    private EditMessageReplyMarkup deleteInlineKeyboard(Barista barista, Integer messageId) {
        return new EditMessageReplyMarkup()
                .setChatId(barista.getChatId())
                .setMessageId(messageId);
    }

    private void sendEditMessageKeyboard(EditMessageReplyMarkup editMessageReplyMarkup, BaristaBot bot) {
        try {
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
