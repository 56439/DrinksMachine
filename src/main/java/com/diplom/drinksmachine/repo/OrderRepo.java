package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    Order findById(long id);
    List<Order> findByUserId(long id);
    List<Order> findByReady(Boolean ready);
    List<Order> findByMenuId(long id);
}
