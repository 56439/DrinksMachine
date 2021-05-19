package com.diplom.drinksmachine.web.controllers;

import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.domain.staff.barista.Shift;
import com.diplom.drinksmachine.repo.BaristaRepo;
import com.diplom.drinksmachine.repo.ShiftRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/shift")
@PreAuthorize("hasAuthority('ADMIN')")
public class ShiftController {

    private final BaristaRepo baristaRepo;
    private final ShiftRepo shiftRepo;
    public ShiftController(BaristaRepo baristaRepo, ShiftRepo shiftRepo) {
        this.baristaRepo = baristaRepo;
        this.shiftRepo = shiftRepo;
    }

    @GetMapping
    public String shift(Model model) {
        setModel(model);
        return "shift";
    }

    @PostMapping
    public String addShift(@RequestParam String date,
                           @RequestParam String barista,
                           Model model
    ) {
        Barista selectedBarista = findBaristaByFIO(barista);
        Date selectedDate = Date.valueOf(date);

        Shift shift = new Shift(selectedDate, selectedBarista);
        shiftRepo.save(shift);
        setModel(model);
        return "shift";
    }

    @PostMapping("/{shift}/delete")
    public String shiftDelete(@PathVariable Shift shift) {
        shiftRepo.delete(shift);
        return "redirect:/shift";
    }

    private Barista findBaristaByFIO(String fio) {
        String[] splitFIO = fio.split(" ");
        return baristaRepo.findByFIO(splitFIO[0],splitFIO[1],splitFIO[2]);
    }

    private void setModel(Model model) {
        List<Barista> barista = baristaRepo.findAll();
        List<Shift> shift = shiftRepo.findAll();
        model.addAttribute("barista", barista);
        model.addAttribute("shift", shift);
    }
}
