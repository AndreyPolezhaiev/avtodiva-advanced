package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.dto.instructor.InstructorCarMaxDate;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
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

    boolean existsByDateAndStartTimeAndInstructorAndCar(
            LocalDate date,
            LocalTime startTime,
            Instructor instructor,
            Car car
    );

    boolean existsByInstructorIdAndCarIdAndDateAndStartTime(
            Long instructorId,
            Long carId,
            LocalDate date,
            LocalTime startTime
    );

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

    @Query("""
       SELECT MAX(s.date)
       FROM ScheduleSlot s
       WHERE s.instructor = :instructor
         AND s.car = :car
         AND s.booked = false
       """)
    LocalDate findMaxFreeDateByInstructorAndCar(@Param("instructor") Instructor instructor, @Param("car") Car car);

    @Query("""
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.instructor i
       JOIN FETCH s.car c
       WHERE s.booked = false
         AND LOWER(i.name) = LOWER(:instructor)
         AND LOWER(c.name) = LOWER(:car)
         AND s.date >= :fromDate
       """)
    List<ScheduleSlot> findFreeSlotsFromDate(
            @Param("instructor") String instructor,
            @Param("car") String car,
            @Param("fromDate") LocalDate fromDate
    );

    @EntityGraph(attributePaths = {"instructor", "car"})
    @Query("""
       SELECT s FROM ScheduleSlot s
       WHERE s.booked = false
       """)
    List<ScheduleSlot> findAllFreeSlots();

    @EntityGraph(attributePaths = {"instructor", "car"})
    @Query("""
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.instructor i
       WHERE s.booked = false
         AND LOWER(i.name) = LOWER(:instructor)
       """)
    List<ScheduleSlot> findAllFreeSlotsByInstructorName(
            @Param("instructor") String instructor
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

    @EntityGraph(attributePaths = {"instructor", "car", "student"})
    List<ScheduleSlot> findAllByStudentId(Long studentId);

    @Query("""
        SELECT new com.polezhaiev.avtodiva.dto.instructor.InstructorCarMaxDate(s.instructor.id, s.car.id, MAX(s.date))
        FROM ScheduleSlot s
        WHERE s.booked = false
        AND s.instructor.id IN :instructorIds
        AND s.car.id IN :carIds
        GROUP BY s.instructor.id, s.car.id
    """)
    List<InstructorCarMaxDate> findAllMaxDatesGrouped(List<Long> instructorIds, List<Long> carIds);

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
