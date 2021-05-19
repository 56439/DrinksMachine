package com.diplom.drinksmachine.baristaBot;

import com.diplom.drinksmachine.baristaBot.handlers.UpdateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@PropertySource("classpath:telegram.properties")
public class BaristaBot extends TelegramLongPollingBot {

    @Value("${bot.barista.name}")
    private String botName;

    @Value("${bot.barista.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private final UpdateHandler updateHandler;
    public BaristaBot(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handler(update, this);
    }
}