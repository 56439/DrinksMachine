package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.staff.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepo extends JpaRepository<Staff, Long> {
    Staff findByUsername(String username);
}
