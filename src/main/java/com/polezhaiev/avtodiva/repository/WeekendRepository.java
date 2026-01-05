package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Weekend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface WeekendRepository extends JpaRepository<Weekend, Long> {
    @Query("""
    SELECT COUNT(w) > 0 FROM Weekend w
    WHERE w.instructor.id = :instructorId
      AND w.day = :date
      AND (w.timeFrom < :timeTo AND w.timeTo > :timeFrom)
    """)
    boolean existsWeekendConflict(
            @Param("instructorId") Long instructorId,
            @Param("date") LocalDate date,
            @Param("timeFrom") LocalTime timeFrom,
            @Param("timeTo") LocalTime timeTo
    );
}
