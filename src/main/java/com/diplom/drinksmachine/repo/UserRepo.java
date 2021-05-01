package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByChatId(long id);
    User findByName(String name);
}
