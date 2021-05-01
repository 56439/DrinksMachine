package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "capacity")
public class Capacity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String symbol;
    private Integer value;

    public Capacity() {}
}
