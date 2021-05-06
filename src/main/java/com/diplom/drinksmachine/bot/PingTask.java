package com.diplom.drinksmachine.bot;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PingTask {
    public static void pingMe() {
        try {
            while (true) {
                try {
                    URL url = new URL("https://www.google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    log.info("Ping {}, {}", url.getHost(), connection.getResponseMessage());
                    connection.disconnect();
                } catch (IOException e) {
                    log.error("Ping FAILED");
                    e.printStackTrace();
                }
                TimeUnit.MINUTES.sleep(20);;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
