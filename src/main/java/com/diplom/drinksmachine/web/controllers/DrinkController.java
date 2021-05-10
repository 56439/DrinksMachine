package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.service.MenuService;
import com.diplom.drinksmachine.service.OrderService;
import com.diplom.drinksmachine.web.urlRequest.UploadImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/drink/{drink}")
@PreAuthorize("hasAuthority('ADMIN')")
public class DrinkController {

    private final MenuService menuService;
    private final OrderService orderService;
    private final UploadImage uploadImage;
    public DrinkController(MenuService menuService, OrderService orderService, UploadImage uploadImage) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.uploadImage = uploadImage;
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
            @RequestParam String description,
            @RequestParam("file") MultipartFile file,
            @PathVariable Drink drink,
            @RequestParam Map<String, String> form,
            @RequestParam(required = false) Integer cost_1,
            @RequestParam(required = false) Integer cost_2,
            @RequestParam(required = false) Integer cost_3
    ) throws Exception {
        if (cost_1 == null && cost_2 == null && cost_3 == null)
            return "redirect:/admin";

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            byte[] img = file.getBytes();
            String image = uploadImage.writeToStore(img);
            drink.setImg(image);
        }

        drink.setTitle(title);
        drink.setDescription(description);
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
        List<Menu> menus = new ArrayList<>();
        for (String key : form.keySet()) {
            for (Capacity capacity : capacities) {
                Menu menu;
                if (capacity.getId().toString().equals(key)) {
                    menu = new Menu(drink, capacity);

                    switch ("cost_" + key) {
                        case "cost_1":
                            if (cost_1 != null) {
                                menu.setCost(cost_1);
                                menus.add(menu);
                            }
                            break;
                        case "cost_2":
                            if (cost_2 != null) {
                                menu.setCost(cost_2);
                                menus.add(menu);
                            }
                            break;
                        case "cost_3":
                            if (cost_3 != null) {
                                menu.setCost(cost_3);
                                menus.add(menu);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if (menus.size() != 0) {
            menuService.addDrink(drink);
            menuService.addMenuList(menus);
        }
    }
}
