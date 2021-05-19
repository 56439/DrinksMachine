package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.service.CafeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@Controller
@RequestMapping("/cafe")
@PreAuthorize("hasAuthority('ADMIN')")
public class CafeController {

    private final CafeService cafeService;
    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping
    public String cafe(Model model) {
        List<Cafe> cafes = cafeService.findAllCafe();
        model.addAttribute("cafes", cafes);
        return "cafe";
    }

    @PostMapping
    public String addCafe(@RequestParam String address,
                          @RequestParam String location,
                          @RequestParam String openTime,
                          @RequestParam String closeTime,
                          Model model
    ) {
        address = "\uD83D\uDCCD " + address;
        openTime += ":00";
        closeTime += ":00";

        Cafe cafe = new Cafe();
        cafe.setAddress(address);
        cafe.setLocation(location);
        cafe.setOpenTime(Time.valueOf(openTime));
        cafe.setCloseTime(Time.valueOf(closeTime));
        cafeService.save(cafe);

        List<Cafe> cafes = cafeService.findAllCafe();
        model.addAttribute("cafes", cafes);

        return "cafe";
    }

    @GetMapping("{cafe}")
    public String cafeEditForm(@PathVariable Cafe cafe, Model model) {
        model.addAttribute("cafe", cafe);
        return "cafeEdit";
    }

    @PostMapping("{cafe}")
    public String cafeEdit(@RequestParam String address,
                           @RequestParam String location,
                           @RequestParam String openTime,
                           @RequestParam String closeTime,
                           @PathVariable Cafe cafe
    ) {
        if (openTime.split(":").length < 3) {
            openTime += ":00";
        }
        if (closeTime.split(":").length < 3) {
            closeTime += ":00";
        }

        cafe.setAddress(address);
        cafe.setLocation(location);
        cafe.setOpenTime(Time.valueOf(openTime));
        cafe.setCloseTime(Time.valueOf(closeTime));
        cafeService.save(cafe);
        return "redirect:/cafe";
    }

    @PostMapping("{cafe}/delete")
    public String cafeDelete(@PathVariable Cafe cafe) {
        cafeService.delete(cafe);
        return "redirect:/cafe";
    }
}
