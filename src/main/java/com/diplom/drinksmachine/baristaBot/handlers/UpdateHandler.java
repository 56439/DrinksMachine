package com.diplom.drinksmachine.baristaBot.handlers;

import com.diplom.drinksmachine.baristaBot.BaristaBot;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.repo.BaristaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UpdateHandler {

    private final BaristaRepo baristaRepo;
    private final BaristaCallbackHandler baristaCallbackHandler;
    private final BaristaAuthorization baristaAuthorization;
    public UpdateHandler(BaristaRepo baristaRepo,
                         BaristaCallbackHandler baristaCallbackHandler,
                         BaristaAuthorization baristaAuthorization) {
        this.baristaRepo = baristaRepo;
        this.baristaCallbackHandler = baristaCallbackHandler;
        this.baristaAuthorization = baristaAuthorization;
    }

    public void handler(Update update, BaristaBot bot) {
        Barista barista = getBarista(update);
        if (barista != null && !barista.isActive()) return;

        if (update.hasCallbackQuery()) {
            baristaCallbackHandler.handler(update, barista, bot);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()){
            baristaAuthorization.handler(update, barista, bot);
        }
    }

    private Barista getBarista(Update update) {
        Long chatId = null;

        if (update.hasMessage())
            chatId = update.getMessage().getChatId();
        if (update.hasCallbackQuery())
            chatId = update.getCallbackQuery().getMessage().getChatId();

        return baristaRepo.findByChatId(chatId);
    }
}
