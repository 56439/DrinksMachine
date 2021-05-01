package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.staff.Role;
import com.diplom.drinksmachine.domain.staff.Staff;
import com.diplom.drinksmachine.repo.StaffRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAuthority('ADMIN')")
public class StaffController {
    private final StaffRepo staffRepo;
    public StaffController(StaffRepo staffRepo) {
        this.staffRepo = staffRepo;
    }

    @GetMapping
    public String staff(Model model) {
        List<Staff> staff = staffRepo.findAll();
        model.addAttribute("staff", staff);
        model.addAttribute("roles", Role.values());
        return "staff";
    }

    @PostMapping
    public String addStaff(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam Map<String, String> form,
                           Model model
    ) {
        Staff staff = new Staff();
        staff.setActive(true);
        staff.setUsername(username);
        staff.setPassword(password);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        Set<Role> chosenRoles = new HashSet<>();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                chosenRoles.add(Role.valueOf(key));
            }
        }
        staff.setRoles(chosenRoles);
        staffRepo.save(staff);

        List<Staff> staffList = staffRepo.findAll();
        model.addAttribute("staff", staffList);
        model.addAttribute("roles", Role.values());
        return "staff";
    }

    @GetMapping("{staff}")
    public String staffEditForm(@PathVariable Staff staff, Model model) {
        model.addAttribute("staff", staff);
        model.addAttribute("roles", Role.values());
        return "staffEdit";
    }

    @PostMapping("{staff}")
    public String staffEdit(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Map<String, String> form,
            @RequestParam("staffId") Staff staff
    ) {
        staff.setUsername(username);
        staff.setPassword(password);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        staff.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                staff.getRoles().add(Role.valueOf(key));
            }
        }
        staffRepo.save(staff);
        return "redirect:/staff";
    }

    @PostMapping("{staff}/delete")
    public String staffDelete(@PathVariable Staff staff) {
        staffRepo.delete(staff);
        return "redirect:/staff";
    }
}
