package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.staff.barista.Barista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaristaRepo extends JpaRepository<Barista, Long> {

    @Query("SELECT b FROM Barista b " +
            "WHERE b.username = ?1 AND b.password = ?2")
    Barista findNewBarista(String login, String password);

    Barista findByChatId(Long chatId);

    @Query("SELECT b FROM Barista b " +
            "WHERE  b.secondName=?1 AND b.firstName=?2 AND b.thirdName=?3")
    Barista findByFIO(String secondName, String firstName, String thirdName);


}
