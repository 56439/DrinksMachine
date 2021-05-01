package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.MenuService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CurrentDrinkHandler {

    final MenuService menuService;
    public CurrentDrinkHandler(MenuService menuService) {
        this.menuService = menuService;
    }

    public SendPhoto chosenDrinkMessage(Update update, User user) {

        long drinkId = Long.parseLong(update.getChosenInlineQuery().getResultId());

        List<Menu> chosenDrinkData = menuService.findMenuByDrinkId(drinkId);
        Drink chosenDrink = chosenDrinkData.get(0).getDrink();
        Capacity capacity = chosenDrinkData.get(0).getCapacity();

        String caption =
                "<b>" + user.getCafe().getAddress() + "</b>\n\n" +
                "› " + chosenDrink.getTitle() + " - " + capacity.getSymbol() +
                ": " + chosenDrinkData.get(0).getCost() + " ₽";

        return new SendPhoto()
                .setParseMode("HTML")
                .setChatId(user.getChatId())
                .setPhoto(chosenDrink.getImg())
                .setCaption(caption)
                .setReplyMarkup(drinkKeyboard(chosenDrinkData));
    }

    private InlineKeyboardMarkup drinkKeyboard(List<Menu> drinkData) {

        Menu selectedDrink = null;
        boolean flag = true;
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        for (Menu m : drinkData) {
            String capacity = m.getCapacity().getSymbol();
            String currentDrinkId = String.valueOf(m.getId());

            if (flag) {
                capacity = "› " + capacity;
                selectedDrink = m;
                flag = false;
            }

            row1.add(new InlineKeyboardButton()
                    .setText(capacity)
                    .setCallbackData("selectdrink" + currentDrinkId));
        }

        row1.add(new InlineKeyboardButton()
                .setText("\uD83C\uDF7D")
                .setSwitchInlineQueryCurrentChat("Меню"));

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(new InlineKeyboardButton().setText("\uD83D\uDCB3 Оплатить (" + selectedDrink.getCost() + " ₽)").setCallbackData("pay" + selectedDrink.getId()));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row3);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }
}
