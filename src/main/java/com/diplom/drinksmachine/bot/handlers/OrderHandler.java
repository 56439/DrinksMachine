package com.diplom.drinksmachine.bot.handlers;

import com.diplom.drinksmachine.baristaBot.BaristaBot;
import com.diplom.drinksmachine.baristaBot.handlers.OrderMessageToBarista;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.service.MenuService;
import com.diplom.drinksmachine.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class OrderHandler {

    @Value("${bot.providerToken}")
    private String providerToken;

    private final OrderService orderService;
    private final MenuService menuService;
    private final BaristaBot baristaBot;
    private final OrderMessageToBarista messageToBarista;
    public OrderHandler(OrderService orderService,
                        MenuService menuService,
                        BaristaBot baristaBot,
                        OrderMessageToBarista messageToBarista) {
        this.orderService = orderService;
        this.menuService = menuService;
        this.baristaBot = baristaBot;
        this.messageToBarista = messageToBarista;
    }

    public SendInvoice paymentMessage(Update update, User user) {
        String callbackText = update.getCallbackQuery().getData();

        String[] parts = callbackText.split(";");

        //String menuId = callbackText.substring(3);
        String menuId = parts[0].substring(3);
        Menu menu = menuService.findById(Long.parseLong(menuId));

        String payload = menuId + ";" + parts[1].substring(4);

        String label = menu.getDrink().getTitle() + " - "
                + menu.getCapacity().getSymbol() + ": "
                + menu.getCapacity().getValue() + " мл.";

        LabeledPrice price = new LabeledPrice();
        price.setLabel(label);
        price.setAmount(Integer.valueOf(menu.getCost() + "00"));
        List<LabeledPrice> prices = new ArrayList<>();
        prices.add(price);

        return new SendInvoice()
                .setChatId(Math.toIntExact(user.getChatId()))
                .setProviderToken(providerToken)
                .setCurrency("RUB")
                .setTitle(menu.getDrink().getTitle())
                .setDescription(menu.getCapacity().getSymbol() + ": "
                        + menu.getCapacity().getValue() + " мл.")
                .setPrices(prices)
                .setPhotoUrl(menu.getDrink().getImg())
                .setPhotoHeight(400)
                .setPhotoWidth(400)
                .setPayload(payload)
                .setStartParameter("startParameter");
    }

    public AnswerPreCheckoutQuery checkoutPayment(Update update) {
        return new AnswerPreCheckoutQuery()
                .setPreCheckoutQueryId(update.getPreCheckoutQuery().getId())
                .setOk(true)
                .setErrorMessage("Платеж не прошел \uD83E\uDD28");
    }

    public void addOrder(String payload, User user) {
        String[] parts = payload.split(";");
        Integer wait = Integer.valueOf(parts[1]);
        long menuId = Long.parseLong(parts[0]);

        Menu menu = menuService.findById(menuId);
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        Order order = new Order();

        order.setCafe(user.getCafe());
        order.setMenu(menu);
        order.setUser(user);
        order.setDate(date);
        order.setWait(wait);

        orderService.addOrder(order);
        messageToBarista.sendOrder(order, baristaBot);
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