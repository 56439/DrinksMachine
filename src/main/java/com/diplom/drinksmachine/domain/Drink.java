package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "drink")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String title;
    private String description;
    private String img;

    public Drink() {}

    public Drink(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Drink(String title, String description, String img) {
        this.title = title;
        this.description = description;
        this.img = img;
    }
}
