package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
@Table(name = "cafe")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String address;
    private String location;

    private Time openTime;
    private Time closeTime;

    public Cafe(){}

    public Cafe(String address, String location, Time openTime, Time closeTime) {
        this.address = address;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
