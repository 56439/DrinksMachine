package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.service.CafeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                          Model model
    ) {
        address = "\uD83D\uDCCD " + address;
        Cafe cafe = new Cafe();
        cafe.setAddress(address);
        cafe.setLocation(location);
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
                           @PathVariable Cafe cafe
    ) {
        cafe.setAddress(address);
        cafe.setLocation(location);
        cafeService.save(cafe);
        return "redirect:/cafe";
    }

    @PostMapping("{cafe}/delete")
    public String cafeDelete(@PathVariable Cafe cafe) {
        cafeService.delete(cafe);
        return "redirect:/cafe";
    }
}
