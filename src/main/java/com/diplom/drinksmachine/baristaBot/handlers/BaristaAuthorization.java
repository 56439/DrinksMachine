package com.diplom.drinksmachine.baristaBot.handlers;

import com.diplom.drinksmachine.baristaBot.BaristaBot;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.repo.BaristaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Component
@Slf4j
public class BaristaAuthorization {

    private final BaristaRepo baristaRepo;
    public BaristaAuthorization(BaristaRepo baristaRepo) {
        this.baristaRepo = baristaRepo;
    }

    public void handler(Update update, Barista barista, BaristaBot bot) {
        Message message = update.getMessage();
        String messageText = message.getText().toLowerCase(Locale.ROOT);
        if (barista == null) {
            if (messageText.equals("/start")) {
                String text = "Здравствуйте!\n" +
                        "Введите логин и пароль в формате <b>«login:password»</b>, без кавычек.";
                sendMessage(message.getChatId(), text, bot);
            } else {
                if (!isValidLoginPassword(messageText)) {
                    String text = "Что-то не сходится.\n" +
                            "Введите свои данные снова.";
                    sendMessage(message.getChatId(), text, bot);
                } else {
                    String[]logPass = messageText.split(":");
                    barista = baristaRepo.findNewBarista(logPass[0],logPass[1]);
                    barista.setActive(true);
                    barista.setChatId(message.getChatId());
                    barista.setTelegramName(message.getFrom().getUserName());
                    baristaRepo.save(barista);

                    String text = "Готово!\n" +
                            "Вы успешно авторизировались!";
                    sendMessage(message.getChatId(), text, bot);

                    log.info("Зарегистрирован новый бариста, ФИО: {} {} {}, telegram: {}",
                            barista.getSecondName(), barista.getFirstName(),
                            barista.getThirdName(), barista.getTelegramName());
                }
            }
        } else {
            String text = "Я вас не понимаю \uD83E\uDDD0";
            sendMessage(message.getChatId(), text, bot);
        }
    }

    protected void sendMessage(Long chatId, String text, BaristaBot bot) {
        SendMessage message = new SendMessage()
                .setParseMode("HTML")
                .setChatId(chatId)
                .setText(text);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidLoginPassword(String loginPassword) {
        if (loginPassword.contains(":")) {
            String[] parts = loginPassword.split(":");
            Barista barista = baristaRepo.findNewBarista(parts[0], parts[1]);
            return barista != null;
        } else return false;
    }
}