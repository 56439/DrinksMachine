package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.service.MenuService;
import com.diplom.drinksmachine.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/drink/{drink}")
@PreAuthorize("hasAuthority('ADMIN')")
public class DrinkController {

    private final MenuService menuService;
    private final OrderService orderService;
    public DrinkController(MenuService menuService, OrderService orderService) {
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @Value("${default.image}")
    private String defaultImage;

    @GetMapping
    public String drinkEditForm(@PathVariable Drink drink, Model model) {

        List<Menu> menu = menuService.findMenuByDrinkId(drink.getId());
        List<Capacity> capacities = menuService.findAllCapacities();

        model.addAttribute("drink", drink);
        model.addAttribute("menu", menu);
        model.addAttribute("capacities", capacities);

        return "drinkEdit";
    }

    @PostMapping
    public String drinkSave(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "Описание отсутствует") String description,
            @RequestParam(required = false, defaultValue = "") String img,
            @PathVariable Drink drink,
            @RequestParam Map<String, String> form,
            @RequestParam(required = false) Integer cost_1,
            @RequestParam(required = false) Integer cost_2,
            @RequestParam(required = false) Integer cost_3
    ) {
        if (cost_1 == null && cost_2 == null && cost_3 == null)
            return "redirect:/admin";

        if (img.equals("")) img = defaultImage;

        drink.setTitle(title);
        drink.setDescription(description);
        drink.setImg(img);
        menuService.updateDrink(drink);

        List<Menu> menu = menuService.findMenuByDrinkId(drink.getId());
        for (Menu m : menu) {
            List<Order> orders = orderService.findOrdersByMenuId(m.getId());
            orderService.deleteOrderList(orders);
        }
        menuService.deleteMenuList(menu);

        addMenu(form, drink, cost_1, cost_2, cost_3);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteDrink(@PathVariable Drink drink) {

        List<Menu> menu = menuService.findMenuByDrinkId(drink.getId());
        for (Menu m : menu) {
            List<Order> orders = orderService.findOrdersByMenuId(m.getId());
            orderService.deleteOrderList(orders);
        }
        menuService.deleteMenuList(menu);
        menuService.deleteDrink(drink);

        return "redirect:/admin";
    }

    public void addMenu(
            Map<String, String> form,
            Drink drink,
            Integer cost_1,
            Integer cost_2,
            Integer cost_3
    ) {
        List<Capacity> capacities = menuService.findAllCapacities();
        for (String key : form.keySet()) {
            for (Capacity capacity : capacities) {
                Menu menu;
                if (capacity.getId().toString().equals(key)) {
                    menu = new Menu(drink, capacity);

                    switch ("cost_" + key) {
                        case "cost_1":
                            menu.setCost(cost_1);
                            break;
                        case "cost_2":
                            menu.setCost(cost_2);
                            break;
                        case "cost_3":
                            menu.setCost(cost_3);
                            break;
                        default:
                            break;
                    }
                    menuService.addMenu(menu);
                }
            }
        }
    }
}
