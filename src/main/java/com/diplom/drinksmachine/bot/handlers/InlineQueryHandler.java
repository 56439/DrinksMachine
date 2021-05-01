package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.service.CafeService;
import com.diplom.drinksmachine.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.List;

@Component
public class InlineQueryHandler {

    final MenuService menuService;
    final CafeService cafeService;
    public InlineQueryHandler(MenuService menuService, CafeService cafeService) {
        this.menuService = menuService;
        this.cafeService = cafeService;
    }

    public AnswerInlineQuery handler(Update update) {
        InlineQuery query = update.getInlineQuery();
        String queryText = query.getQuery();

        if (!queryText.equals("Меню")) return null;

        return menuList(query);
    }

    private AnswerInlineQuery menuList(InlineQuery query) {
        List<Drink> drinkList = menuService.findAllDrinks();

        InlineQueryResult[] resArray = new InlineQueryResult[drinkList.size()];

         for (int i = 0; i < resArray.length; i++) {
             InputTextMessageContent inputTextMessageContent = new InputTextMessageContent()
                     .setMessageText(String.valueOf(drinkList.get(i).getId()));

             Drink drink = drinkList.get(i);
             StringBuilder description = new StringBuilder()
                     .append(drink.getDescription())
                     .append("\n");

             List<Menu> drinkCapacity = menuService.findMenuByDrinkId(drink.getId());
             for (Menu capacity : drinkCapacity) {
                 description
                         .append(capacity.getCapacity().getValue())
                         .append(" мл. / ");
             }

             resArray[i] = new InlineQueryResultArticle()
                     .setId(drink.getId().toString())
                     .setTitle(drink.getTitle())
                     .setDescription(description.substring(0,description.length()-2))
                     .setThumbUrl(drink.getImg())
                     .setInputMessageContent(inputTextMessageContent);
         }

         return new AnswerInlineQuery()
                 .setInlineQueryId(query.getId())
                 .setResults(resArray)
                 .setPersonal(true);
    }
}