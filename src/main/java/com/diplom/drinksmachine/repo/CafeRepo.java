package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepo extends JpaRepository<Cafe, Long> {
    Cafe findByAddress(String address);
    Cafe findById(long id);
}
