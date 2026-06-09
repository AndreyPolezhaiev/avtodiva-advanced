package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.dto.schedule.generation.InstructorCarMaxDateDto;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long>, JpaSpecificationExecutor<ScheduleSlot> {

    @Override
    @EntityGraph(attributePaths = {"instructor", "car", "student"})
    @NonNull
    List<ScheduleSlot> findAll(@Nullable Specification<ScheduleSlot> spec);

    @EntityGraph(attributePaths = {"instructor", "car", "student"})
    Optional<ScheduleSlot> findSlotById(Long id);

    @Query("""
    SELECT COUNT(s) > 0 FROM ScheduleSlot s
    WHERE (s.car.id = :carId OR s.instructor.id = :instructorId)
      AND s.date = :date
      AND s.booked = true
      AND s.startTime < :endTime
      AND s.endTime > :startTime
      AND (:excludeId IS NULL OR s.id <> :excludeId)
    """)
    boolean existsBookedInstructorAndCarConflictExcluding(
            @Param("instructorId") Long instructorId,
            @Param("carId") Long carId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId
    );

    @Query("""
    SELECT COUNT(s) > 0 FROM ScheduleSlot s
    WHERE s.date = :date
      AND s.booked = true
      AND (s.startTime < :endTime AND s.endTime > :startTime)
      AND (s.car.id = :carId OR s.instructor.id = :instructorId)
    """)
    boolean existsBookedInstructorAndCarConflict(
            @Param("instructorId") Long instructorId,
            @Param("carId") Long carId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @EntityGraph(attributePaths = {"instructor", "car"})
    @Query("""
       SELECT s FROM ScheduleSlot s
       WHERE s.student.id = :studentId
       AND s.booked = true
       ORDER BY s.date DESC, s.startTime DESC
       LIMIT 1
       """)
    Optional<ScheduleSlot> findLastBookedStudentSlot(Long studentId);

    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE ScheduleSlot s
    SET s.student = null,
        s.description = null,
        s.link = null,
        s.booked = false
    WHERE s.student.id = :studentId
    """)
    void releaseAllSlotsByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Query("UPDATE ScheduleSlot s SET s.isDeleted = true WHERE s.car.id = :carId")
    void softDeleteAllByCarId(@Param("carId") Long carId);

    @Modifying
    @Query("UPDATE ScheduleSlot s SET s.isDeleted = true WHERE s.instructor.id = :instructorId")
    void softDeleteAllByInstructorId(@Param("instructorId") Long instructorId);

    @Query("""
        SELECT new com.polezhaiev.avtodiva.dto.schedule.generation.InstructorCarMaxDateDto(s.instructor.id, s.car.id, MAX(s.date))
        FROM ScheduleSlot s
        WHERE s.booked = false
        AND s.instructor.id IN :instructorIds
        AND s.car.id IN :carIds
        GROUP BY s.instructor.id, s.car.id
    """)
    List<InstructorCarMaxDateDto> findAllMaxDatesGrouped(List<Long> instructorIds, List<Long> carIds);

    @Query("""
        SELECT s FROM ScheduleSlot s
        WHERE s.date BETWEEN :startDate AND :endDate
        AND s.instructor.id IN :instructorIds
        AND s.car.id IN :carIds
    """)
    List<ScheduleSlot> findFilteredByDateBetween(
            LocalDate startDate,
            LocalDate endDate,
            List<Long> instructorIds,
            List<Long> carIds
    );
}
