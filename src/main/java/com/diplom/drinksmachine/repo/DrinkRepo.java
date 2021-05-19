package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepo extends JpaRepository<Drink, Long> {
    Drink findById(long id);
}
