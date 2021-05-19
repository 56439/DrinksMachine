package com.diplom.drinksmachine.domain.staff.barista;

import com.diplom.drinksmachine.domain.Cafe;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "barista")
public class Barista {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String secondName;
    private String thirdName;

    private String username;
    private String password;
    private boolean active = false;

    private Long chatId;
    private String telegramName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    public Barista() {}

    public Barista(String firstName,
                   String secondName,
                   String thirdName,
                   String username,
                   String password,
                   Cafe cafe) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.username = username;
        this.password = password;
        this.cafe = cafe;
    }
}
