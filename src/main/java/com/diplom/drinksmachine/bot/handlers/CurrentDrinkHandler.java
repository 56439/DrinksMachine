package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrentDrinkHandler {

    private final MenuService menuService;
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

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(new InlineKeyboardButton()
                .setText("✅ Подтвердить")
                .setCallbackData("confirm" + selectedDrink.getId()));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    public EditMessageReplyMarkup timeKeyboard(Update update, User user) {
        String callback = update.getCallbackQuery().getData().substring(7);
        Message message = update.getCallbackQuery().getMessage();
        Menu selectedDrink = menuService.findById(Long.parseLong(callback));

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(new InlineKeyboardButton()
                .setText("› \uD83D\uDD25")
                .setCallbackData("wait0"));
        row1.add(new InlineKeyboardButton()
                .setText("\uD83D\uDD50 5")
                .setCallbackData("wait5"));
        row1.add(new InlineKeyboardButton()
                .setText("\uD83D\uDD51 10")
                .setCallbackData("wait10"));
        row1.add(new InlineKeyboardButton()
                .setText("\uD83D\uDD52 15")
                .setCallbackData("wait15"));

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(new InlineKeyboardButton()
                .setText("\uD83D\uDCB3 Перейти к оплате (" + selectedDrink.getCost() + " ₽)")
                .setCallbackData("pay" + selectedDrink.getId() + ";wait0"));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return new EditMessageReplyMarkup()
                .setChatId(user.getChatId())
                .setMessageId(message.getMessageId())
                .setReplyMarkup(inlineKeyboardMarkup);
    }
}