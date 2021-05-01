package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "cafe")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String address;
    private String location;

    public Cafe(){}

    public Cafe(String address, String location) {
        this.address = address;
        this.location = location;
    }
}
