package com.diplom.drinksmachine.domain.staff.barista;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "barista_id")
    private Barista barista;

    public Shift() {}

    public Shift(Date date, Barista barista) {
        this.date = date;
        this.barista = barista;
    }
}
