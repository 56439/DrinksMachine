package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.service.MenuService;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final MenuService menuService;
    private final DrinkController drinkController;

    public AdminController(MenuService menuService, DrinkController drinkController) {
        this.menuService = menuService;
        this.drinkController = drinkController;
    }

    @Value("${default.image}")
    private String defaultImage;

    @GetMapping
    public String admin(Model model) {
        List<Drink> drinks = menuService.findAllDrinks();
        List<Capacity> capacities = menuService.findAllCapacities();

        model.addAttribute("drinks", drinks);
        model.addAttribute("capacities", capacities);
        return "admin";
    }

    @PostMapping
    public String addDrink(Model model,
                           @RequestParam String title,
                           @RequestParam(required = false, defaultValue = "Описание отсутствует") String description,
                           @RequestParam(required = false, defaultValue = "") String image,
                           @RequestParam Map<String, String> form,
                           @RequestParam(required = false) Integer cost_1,
                           @RequestParam(required = false) Integer cost_2,
                           @RequestParam(required = false) Integer cost_3
    ) {
        if (cost_1 == null && cost_2 == null && cost_3 == null)
            return "admin";

        if (image.equals("")) image = defaultImage;

        Drink drink = new Drink(title, description, image);
        menuService.addDrink(drink);

        drink = menuService.findLastAddedDrink();
        drinkController.addMenu(form, drink, cost_1, cost_2, cost_3);

        List<Drink> drinks = menuService.findAllDrinks();
        List<Capacity> capacities = menuService.findAllCapacities();
        model.addAttribute("drinks", drinks);
        model.addAttribute("capacities", capacities);

        return "admin";
    }
}