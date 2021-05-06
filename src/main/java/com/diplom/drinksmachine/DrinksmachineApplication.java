package com.diplom.drinksmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class DrinksmachineApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DrinksmachineApplication.class, args);
    }
}