package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.service.OrderReadyMessage;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/barista")
@PreAuthorize("hasAuthority('BARISTA')")
public class BaristaController {
    private final OrderService orderService;
    private final OrderReadyMessage orderReadyMessage;
    public BaristaController(OrderService orderService, OrderReadyMessage orderReadyMessage) {
        this.orderService = orderService;
        this.orderReadyMessage = orderReadyMessage;
    }

    @GetMapping
    public String barista(Model model) {
        List<Order> orders = orderService.findOrdersByReady(false);
        model.addAttribute("orders", orders);
        return "barista";
    }

    @PostMapping
    public String readyOrder(
            Map<String, Object> model,
            @RequestParam Map<String, String> form
    ) {
        List<String> idList = orderService.findAllId();
        for (String key : form.keySet()) {
            if (idList.contains(key)) {
                Order order = orderService.findById(Long.parseLong(key));
                order.setReady(true);
                orderService.updateOrder(order);
                orderReadyMessage.pingMe(order);
            }
        }

        List<Order> orders = orderService.findOrdersByReady(false);
        model.put("orders", orders);

        return "barista";
    }


}
