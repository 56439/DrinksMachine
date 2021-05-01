package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capacity_id")
    private Capacity capacity;

    private Integer cost;

    public Menu() {}

    public Menu(Drink drink, Capacity capacity) {
        this.drink = drink;
        this.capacity = capacity;
    }

    public Menu(Drink drink, Capacity capacity, int cost) {
        this.drink = drink;
        this.capacity = capacity;
        this.cost = cost;
    }
}
