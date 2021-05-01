package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacityRepo extends JpaRepository<Capacity, Long> {
    Capacity findById(long id);
}
