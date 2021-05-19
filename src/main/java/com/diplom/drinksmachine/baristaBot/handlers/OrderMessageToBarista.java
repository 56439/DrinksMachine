package com.diplom.drinksmachine.baristaBot.handlers;

import com.diplom.drinksmachine.baristaBot.BaristaBot;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.repo.ShiftRepo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMessageToBarista {
    private final ShiftRepo shiftRepo;
    public OrderMessageToBarista(ShiftRepo shiftRepo) {
        this.shiftRepo = shiftRepo;
    }

    public void sendOrder(Order order, BaristaBot bot) {
        new Thread(()->{
            try {
                Thread.sleep(order.getWait() * 60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String text = "<b>НОВЫЙ ЗАКАЗ №" + order.getId() + "</b>\n\n"
                    + order.getMenu().getDrink().getTitle() + " - "
                    + order.getMenu().getCapacity().getSymbol() + ": "
                    + order.getMenu().getCapacity().getValue() + " мл.";

            Barista barista = selectedBarista(order);

            SendMessage sendMessage = new SendMessage()
                    .setChatId(barista.getChatId())
                    .setParseMode("HTML")
                    .setText(text)
                    .setReplyMarkup(inlineReadyButton(order.getId()));

            sendMessage(sendMessage, bot);
        }).start();
    }

    private Barista selectedBarista(Order order) {
        return shiftRepo.findBarista(order.getCafe(), order.getDate());
    }

    private InlineKeyboardMarkup inlineReadyButton(long orderId) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton()
                .setText("✅ Готово")
                .setCallbackData("ready" + orderId));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    private void sendMessage(SendMessage message, BaristaBot bot) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
