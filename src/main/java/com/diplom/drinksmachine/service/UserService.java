package com.diplom.drinksmachine.service;

import com.diplom.drinksmachine.domain.User;
import com.diplom.drinksmachine.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public User findByUserName(String name) {
        return userRepo.findByName(name);
    }

    @Transactional
    public void addUser(User user) {
        userRepo.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepo.save(user);
    }
}