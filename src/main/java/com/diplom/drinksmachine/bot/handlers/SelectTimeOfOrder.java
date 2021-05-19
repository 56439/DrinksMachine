package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class SelectTimeOfOrder {

    public EditMessageReplyMarkup handler(Update update, User user) {
        String callback = update.getCallbackQuery().getData().substring(4);
        Message message = update.getCallbackQuery().getMessage();

        List<List<InlineKeyboardButton>> keyboard = message.getReplyMarkup().getKeyboard();

        for (int i = 0; i < keyboard.get(0).toArray().length; i++) {
            InlineKeyboardButton button = keyboard.get(0).get(i);

            if (button.getText().contains("› ")) {
                button.setText(button.getText().substring(2));
            }

            if (button.getCallbackData().substring(4).equals(callback)) {
                button.setText("› " + button.getText());

                InlineKeyboardButton payButton = keyboard.get(1).get(0);
                payButton = changePayButtonCallback(payButton, button.getCallbackData());
                keyboard.get(1).set(0, payButton);
            }
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);

        return  new EditMessageReplyMarkup()
                .setChatId(user.getChatId())
                .setMessageId(message.getMessageId())
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    private InlineKeyboardButton changePayButtonCallback(InlineKeyboardButton payButton, String newPart) {
        String callback = payButton.getCallbackData();
        String[] parts = callback.split(";");
        parts[1] = newPart;
        callback = parts[0] + ";" + parts[1];
        return payButton.setCallbackData(callback);
    }
}
