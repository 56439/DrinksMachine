package com.diplom.drinksmachine.repo;

import com.diplom.drinksmachine.domain.Cafe;
import com.diplom.drinksmachine.domain.staff.barista.Barista;
import com.diplom.drinksmachine.domain.staff.barista.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface ShiftRepo extends JpaRepository<Shift, Long> {
    @Query("SELECT s.barista FROM Shift s " +
            "WHERE s.barista.cafe=?1 AND s.date=?2")
    Barista findBarista(Cafe cafe, Date date);
}
