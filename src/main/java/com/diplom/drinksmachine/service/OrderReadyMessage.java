package com.diplom.drinksmachine.service;

import com.diplom.drinksmachine.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class OrderReadyMessage {

    @Value("${bot.token}")
    private String botToken;

    public void pingMe(Order order) {
        String chatId = String.valueOf(order.getUser().getChatId());
        String messageText = "<b>Ваш заказ №" + order.getId() + " готов</b>%0A%0A"
                + order.getCafe().getAddress() + "%0A%0A"
                + order.getMenu().getDrink().getTitle() + " - "
                + order.getMenu().getCapacity().getSymbol() + ": "
                + order.getMenu().getCapacity().getValue() + " мл.";

        String urlRequest = "https://api.telegram.org/bot" + botToken
                + "/sendMessage?chat_id=" + chatId
                + "&parse_mode=HTML"
                + "&text=" + messageText;

        try {
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            log.info("Request {}, {}", url.getHost(), connection.getResponseMessage());
            connection.disconnect();
        } catch (IOException e) {
            log.error("Request FAILED");
            e.printStackTrace();
        }
    }
}