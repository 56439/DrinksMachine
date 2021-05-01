package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.MenuService;
import com.diplom.drinksmachine.service.OrderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderHandler {
    final OrderService orderService;
    final MenuService menuService;
    public OrderHandler(OrderService orderService, MenuService menuService) {
        this.orderService = orderService;
        this.menuService = menuService;
    }

    public EditMessageReplyMarkup handler(Update update, User user) {
        String callbackText = update.getCallbackQuery().getData();
        String drinkId = callbackText.substring(3);
        Menu menu = menuService.findById(Long.parseLong(drinkId));

        Order order = new Order();
        order.setCafe(user.getCafe());
        order.setMenu(menu);
        order.setUser(user);
        orderService.addOrder(order);

        List<Order> orders = orderService.findByUserId(user.getId());
        Long orderId = orders.get(orders.size()-1).getId();

        return new EditMessageReplyMarkup()
                .setChatId(user.getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setReplyMarkup(checkOrderKeyboard(orderId));
    }

    public AnswerCallbackQuery checkOrder(Update update) {
        String callbackText = update.getCallbackQuery().getData();
        long orderId = Long.parseLong(callbackText.substring(10));

        Order order = orderService.findById(orderId);
        String answerText;

        if (order == null) {
            answerText = "Заказ не найден";
        } else {
            if (order.getReady())
                answerText = "Ваш заказ уже готов и ждет вас!\n\nЗаказ №" + orderId;
            else
                answerText = "Ваш заказ пока не готов :(\n\nЗаказ №" + orderId;
        }

        return new AnswerCallbackQuery()
                .setShowAlert(true)
                .setCallbackQueryId(update.getCallbackQuery().getId())
                .setText(answerText);
    }

    private InlineKeyboardMarkup checkOrderKeyboard(Long orderId) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton()
                .setText("❓ Состояние заказа")
                .setCallbackData("checkorder" + orderId));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    public EditMessageText orderInfo(Update update, User user) {
        String orderId = update.getCallbackQuery().getData().substring(9);
        Order order = orderService.findById(Long.parseLong(orderId));

        String status;
        if (order.getReady()) status = "Готов";
        else status = "Не готов";

        String messageText = "<b>Заказ №" + order.getId() + "</b>\n\n" +
                order.getMenu().getDrink().getTitle() + " - " +
                order.getMenu().getCapacity().getSymbol() + ": " +
                order.getMenu().getCost() + "₽\n\n" +
                "Статус: <b>" + status + "</b>";

        return new EditMessageText()
                .setParseMode("HTML")
                .setChatId(user.getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setText(messageText)
                .setReplyMarkup(orderInfoKeyboard());
    }

    private InlineKeyboardMarkup orderInfoKeyboard() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton()
            .setText("◀ Назад")
            .setCallbackData("backtoorderlist"));

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }

    public EditMessageText orderList(Update update, User user) {
        StringBuilder messageText = getOrderList(user);

        return new EditMessageText()
                .setParseMode("HTML")
                .setText(String.valueOf(messageText))
                .setChatId(user.getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setReplyMarkup(orderListKeyboard(user));
    }

    public StringBuilder getOrderList(User user) {
        List<Order> myOrders = orderService.findByUserId(user.getId());
        StringBuilder messageText = new StringBuilder("<b>Мои заказы:</b>\n");
        int i = 1;

        for (Order order : myOrders) {
            messageText.append("<b>").append(i)
                    .append(")</b> №")
                    .append(order.getId())
                    .append(" | ")
                    .append(order.getMenu().getDrink().getTitle())
                    .append(" - ")
                    .append(order.getMenu().getCapacity().getSymbol())
                    .append("\n");
            i++;
        }
        return messageText;
    }

    public InlineKeyboardMarkup orderListKeyboard(User user) {

        List<Order> myOrders = orderService.findByUserId(user.getId());
        List<InlineKeyboardButton> row = new ArrayList<>();
        int i = 1;

        for (Order order : myOrders) {
            row.add(new InlineKeyboardButton()
                    .setText(String.valueOf(i))
                    .setCallbackData("orderinfo" + order.getId()));
            i++;
        }

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        return new InlineKeyboardMarkup().setKeyboard(rowList);
    }
}