package com.diplom.drinksmachine.domain;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private Long chatId;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    public User(){}

    public User(Long chatId, String name){
        this.chatId=chatId;
        this.name=name;
    }
}
