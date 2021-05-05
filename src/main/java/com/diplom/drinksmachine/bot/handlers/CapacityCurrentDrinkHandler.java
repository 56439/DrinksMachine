package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class CapacityCurrentDrinkHandler {

    final MenuService menuService;
    public CapacityCurrentDrinkHandler(MenuService menuService) {
        this.menuService = menuService;
    }

    public EditMessageCaption handler(Update update, User user) {

        String callback = update.getCallbackQuery().getData().substring(11);
        Message message = update.getCallbackQuery().getMessage();

        List<List<InlineKeyboardButton>> keyboard = message.getReplyMarkup().getKeyboard();
        InlineKeyboardButton payButton = keyboard.get(1).get(0);

        String caption = "";

        Menu chosenDrink = menuService.findById(Long.parseLong(callback));

        for(int i = 0; i < keyboard.get(0).toArray().length-1; i++) {
            InlineKeyboardButton button = keyboard.get(0).get(i);

            if (button.getCallbackData().substring(11).equals(callback)) {
                button.setText("› " + chosenDrink.getCapacity().getSymbol());
                payButton.setCallbackData("pay" + callback);
                payButton.setText("\uD83D\uDCB3 Перейти к оплате (" + chosenDrink.getCost() + " ₽)");

                caption =
                        "<b>" + user.getCafe().getAddress() + "</b>\n\n" +
                                "› " + chosenDrink.getDrink().getTitle() + " - " + chosenDrink.getCapacity().getSymbol() +
                                ": " + chosenDrink.getCost() + " ₽";

            } else {
                if (button.getText().length() > 1) {
                    button.setText(button.getText().substring(1));
                }
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);

        return new EditMessageCaption()
                .setChatId(String.valueOf(user.getChatId()))
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setParseMode("HTML")
                .setCaption(caption)
                .setReplyMarkup(inlineKeyboardMarkup);
    }
}
