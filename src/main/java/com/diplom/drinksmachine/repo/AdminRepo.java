package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.staff.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {
}
