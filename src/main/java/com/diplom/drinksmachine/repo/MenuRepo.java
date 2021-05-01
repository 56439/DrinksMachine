package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepo extends JpaRepository<Menu, Long> {
    Menu findById(long id);
    List<Menu> findByDrinkId(long id);
}
