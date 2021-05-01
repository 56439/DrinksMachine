package com.diplom.drinksmachine.service;

import com.diplom.drinksmachine.domain.Order;
import com.diplom.drinksmachine.repo.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Transactional
    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(long id) {
        return orderRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(long id) {
        return orderRepo.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<String> findAllId() {
        List<Order> orders = orderRepo.findAll();
        List<String> idList = new ArrayList<>();
        for (Order order : orders) {
            idList.add(String.valueOf(order.getId()));
        }
        return idList;
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByReady(boolean ready) {
        return orderRepo.findByReady(ready);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByMenuId(long id) {
        return orderRepo.findByMenuId(id);
    }

    @Transactional
    public void addOrder(Order order) {
        order.setReady(false);
        orderRepo.save(order);
    }

    @Transactional
    public void updateOrder(Order order) {
        orderRepo.save(order);
    }

    @Transactional
    public void deleteOrderList(List<Order> order) {
        orderRepo.deleteAll(order);
    }
}
