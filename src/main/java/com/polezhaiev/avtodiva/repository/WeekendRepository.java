package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Weekend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeekendRepository extends JpaRepository<Weekend, Long> {
    @Query("SELECT w FROM Weekend w JOIN FETCH w.instructor")
    List<Weekend> findAllWithInstructor();

    @Query("SELECT w FROM Weekend w JOIN FETCH w.instructor WHERE w.id = :id")
    Optional<Weekend> findByIdWithInstructor(@Param("id") Long id);

    @Query("""
    SELECT COUNT(w) > 0 FROM Weekend w
    WHERE w.instructor.id = :instructorId
      AND w.date = :date
      AND (w.startTime < :endTime AND w.endTime > :startTime)
    """)
    boolean existsWeekendConflict(
            @Param("instructorId") Long instructorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
