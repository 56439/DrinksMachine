package com.diplom.drinksmachine.service;

import com.diplom.drinksmachine.domain.Capacity;
import com.diplom.drinksmachine.domain.Drink;
import com.diplom.drinksmachine.domain.Menu;
import com.diplom.drinksmachine.repo.CapacityRepo;
import com.diplom.drinksmachine.repo.DrinkRepo;
import com.diplom.drinksmachine.repo.MenuRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepo menuRepo;
    private final DrinkRepo drinkRepo;
    private final CapacityRepo capacityRepo;

    public MenuService(MenuRepo menuRepo, DrinkRepo drinkRepo, CapacityRepo capacityRepo) {
        this.menuRepo = menuRepo;
        this.drinkRepo = drinkRepo;
        this.capacityRepo = capacityRepo;
    }

    @Transactional(readOnly = true)
    public Menu findById(long id) {
        return menuRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Drink> findAllDrinks() {
        return drinkRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Capacity> findAllCapacities() {
        return capacityRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Menu> findMenuByDrinkId(long id) {
        return menuRepo.findByDrinkId(id);
    }

    @Transactional
    public void addDrink(Drink drink) {
        drinkRepo.save(drink);
    }

    @Transactional
    public void updateDrink(Drink drink) {
        drinkRepo.save(drink);
    }

    @Transactional
    public void deleteDrink(Drink drink) {
        drinkRepo.delete(drink);
    }

    @Transactional
    public void addMenu(Menu menu) {
        menuRepo.save(menu);
    }

    @Transactional
    public void addMenuList(List<Menu> menus) {
        menuRepo.saveAll(menus);
    }

    @Transactional
    public void deleteMenuList(List<Menu> menu) {
        menuRepo.deleteAll(menu);
    }
}
