package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long>, JpaSpecificationExecutor<ScheduleSlot> {
    boolean existsByDateAndTimeFromAndInstructorAndCar(
            LocalDate date,
            LocalTime timeFrom,
            Instructor instructor,
            Car car
    );

    boolean existsByInstructorIdAndCarIdAndDateAndTimeFrom(
            Long instructorId,
            Long carId,
            LocalDate date,
            LocalTime timeFrom
    );

    @Query("""
    SELECT COUNT(s) > 0 FROM ScheduleSlot s
    WHERE (s.car.id = :carId OR s.instructor.id = :instructorId)
      AND s.date = :date
      AND s.booked = true
      AND s.timeFrom < :timeTo
      AND s.timeTo > :timeFrom
      AND (:excludeId IS NULL OR s.id <> :excludeId)
    """)
    boolean existsBookedInstructorAndCarConflictExcluding(
            @Param("instructorId") Long instructorId,
            @Param("carId") Long carId,
            @Param("date") LocalDate date,
            @Param("timeFrom") LocalTime timeFrom,
            @Param("timeTo") LocalTime timeTo,
            @Param("excludeId") Long excludeId
    );

    @Query("""
    SELECT COUNT(s) > 0 FROM ScheduleSlot s
    WHERE s.date = :date
      AND s.booked = true
      AND (s.timeFrom < :timeTo AND s.timeTo > :timeFrom)
      AND (s.car.id = :carId OR s.instructor.id = :instructorId)
    """)
    boolean existsBookedInstructorAndCarConflict(
            @Param("instructorId") Long instructorId,
            @Param("carId") Long carId,
            @Param("date") LocalDate date,
            @Param("timeFrom") LocalTime timeFrom,
            @Param("timeTo") LocalTime timeTo
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

    @Query("""
       SELECT s FROM ScheduleSlot s
       WHERE s.booked = false
       """)
    List<ScheduleSlot> findAllFreeSlots();

    @Query("""
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.instructor i
       WHERE s.booked = false
         AND LOWER(i.name) = LOWER(:instructor)
       """)
    List<ScheduleSlot> findAllFreeSlotsByInstructorName(
            @Param("instructor") String instructor
    );
}
