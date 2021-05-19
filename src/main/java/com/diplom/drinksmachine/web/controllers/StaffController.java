package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.domain.staff.admin.Admin;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.domain.staff.admin.Role;
import com.diplom.drinksmachine.repo.AdminRepo;
import com.diplom.drinksmachine.repo.BaristaRepo;
import com.diplom.drinksmachine.service.CafeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAuthority('ADMIN')")
public class StaffController {
    private final AdminRepo adminRepo;
    private final BaristaRepo baristaRepo;
    private final CafeService cafeService;
    public StaffController(AdminRepo adminRepo, BaristaRepo baristaRepo, CafeService cafeService) {
        this.adminRepo = adminRepo;
        this.baristaRepo = baristaRepo;
        this.cafeService = cafeService;
    }

    @GetMapping
    public String staff(Model model) {
        setModel(model);
        return "staff";
    }

    @PostMapping
    public String addStaff(@RequestParam String firstName,
                           @RequestParam String secondName,
                           @RequestParam String thirdName,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String cafe,
                           @RequestParam Map<String, String> form,
                           Model model
    ) {
        for (String key : form.keySet()) {
            if (key.equals("role")) {
                String keyValue = form.getOrDefault(key, "");
                switch (keyValue) {
                    case "admin":
                        Admin admin = new Admin(firstName, secondName, thirdName,
                                username.toLowerCase(Locale.ROOT), password);
                        admin.setRoles(Collections.singleton(Role.ADMIN));
                        adminRepo.save(admin);
                        break;
                    case "barista":
                        Cafe selectedCafe = cafeService.findByAddress(cafe);
                        Barista barista = new Barista(
                                firstName, secondName,
                                thirdName, username.toLowerCase(Locale.ROOT),
                                password, selectedCafe);
                        baristaRepo.save(barista);
                        break;
                    default:
                        break;
                }
            }
        }
        setModel(model);
        return "staff";
    }

    @GetMapping("{admin}")
    public String staffEditForm(@PathVariable Admin admin, Model model) {
        model.addAttribute("admin", admin);
        return "adminEdit";
    }

    @PostMapping("{admin}")
    public String staffEdit(@RequestParam String firstName,
                            @RequestParam String secondName,
                            @RequestParam String thirdName,
                            @RequestParam String username,
                            @RequestParam String password,
                            @PathVariable Admin admin
    ) {
        admin.setFirstName(firstName);
        admin.setSecondName(secondName);
        admin.setThirdName(thirdName);
        admin.setUsername(username.toLowerCase(Locale.ROOT));
        admin.setPassword(password);

        adminRepo.save(admin);
        return "redirect:/staff";
    }

    @PostMapping("{admin}/delete")
    public String staffDelete(@PathVariable Admin admin) {
        adminRepo.delete(admin);
        return "redirect:/staff";
    }

    @GetMapping("/barista/{barista}")
    public String baristaEditForm(@PathVariable Barista barista, Model model) {
        List<Cafe> cafe = cafeService.findAllCafe();
        model.addAttribute("barista", barista);
        model.addAttribute("cafe", cafe);
        return "baristaEdit";
    }

    @PostMapping("/barista/{barista}")
    public String baristaEdit(@RequestParam String firstName,
                              @RequestParam String secondName,
                              @RequestParam String thirdName,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String cafe,
                              @PathVariable Barista barista
    ) {
        Cafe selectedCafe = cafeService.findByAddress(cafe);
        barista.setFirstName(firstName);
        barista.setSecondName(secondName);
        barista.setThirdName(thirdName);
        barista.setUsername(username.toLowerCase(Locale.ROOT));
        barista.setPassword(password);
        barista.setCafe(selectedCafe);

        baristaRepo.save(barista);
        return "redirect:/staff";
    }

    @PostMapping("/barista/{barista}/delete")
    public String baristaDelete(@PathVariable Barista barista) {
        baristaRepo.delete(barista);
        return "redirect:/staff";
    }

    private void setModel(Model model) {
        List<Admin> admin = adminRepo.findAll();
        List<Barista> barista = baristaRepo.findAll();
        List<Cafe> cafe = cafeService.findAllCafe();
        model.addAttribute("admin", admin);
        model.addAttribute("barista", barista);
        model.addAttribute("cafe", cafe);
    }
}
