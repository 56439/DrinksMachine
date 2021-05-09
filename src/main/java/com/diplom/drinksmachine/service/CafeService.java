package com.diplom.drinksmachine.service;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.repo.CafeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CafeService {

    private final CafeRepo cafeRepo;

    public CafeService(CafeRepo cafeRepo) {
        this.cafeRepo = cafeRepo;
    }

    @Transactional(readOnly = true)
    public List<Cafe> findAllCafe() {
        return cafeRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Cafe findByAddress(String address) {
        return cafeRepo.findByAddress(address);
    }

    @Transactional(readOnly = true)
    public Cafe findById(long id) {
        return cafeRepo.findById(id);
    }

    @Transactional
    public void save(Cafe cafe) {
        cafeRepo.save(cafe);
    }

    @Transactional
    public void  delete(Cafe cafe) {
        cafeRepo.delete(cafe);
    }
}
