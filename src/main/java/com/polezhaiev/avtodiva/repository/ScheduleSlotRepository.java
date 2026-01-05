package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {

    @Query("""
            SELECT s FROM ScheduleSlot s
            JOIN FETCH s.instructor i
            JOIN FETCH s.car c
            WHERE s.booked = true
              AND LOWER(i.name) IN :instructorNames
              AND LOWER(c.name) IN :carNames
              AND s.date BETWEEN :start AND :end
              AND NOT EXISTS (
                  SELECT w FROM Weekend w
                  WHERE w.instructor = s.instructor
                    AND w.day = s.date
                    AND s.timeFrom < w.timeTo
                    AND s.timeTo > w.timeFrom
              )
            """)
    List<ScheduleSlot> findBookedSlotsBetween(
            @Param("instructorNames") List<String> instructorNames,
            @Param("carNames") List<String> carNames,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);


    @Query("""
            SELECT s FROM ScheduleSlot s
            JOIN FETCH s.instructor i
            JOIN FETCH s.car c
            WHERE s.booked = false
              AND LOWER(i.name) IN :instructorNames
              AND LOWER(c.name) IN :carNames
              AND s.date BETWEEN :start AND :end
              AND NOT EXISTS (
                  SELECT w FROM Weekend w
                  WHERE w.instructor = s.instructor
                    AND w.day = s.date
                    AND s.timeFrom < w.timeTo
                    AND s.timeTo > w.timeFrom
              )
              AND NOT EXISTS (
                  SELECT s2 FROM ScheduleSlot s2
                  WHERE s2.car = s.car
                    AND s2.date = s.date
                    AND s2.booked = true
                    AND s2.timeFrom < s.timeTo
                    AND s2.timeTo > s.timeFrom
              )
              AND NOT EXISTS (
                            SELECT s3 FROM ScheduleSlot s3
                            WHERE s3.instructor = s.instructor
                              AND s3.date = s.date
                              AND s3.booked = true
                              AND s3.timeFrom < s.timeTo
                              AND s3.timeTo > s.timeFrom
              )
            """)
    List<ScheduleSlot> findFreeSlotsBetween(
            @Param("instructorNames") List<String> instructorNames,
            @Param("carNames") List<String> carNames,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);


    @Query("""
            SELECT s FROM ScheduleSlot s
            JOIN FETCH s.instructor i
            JOIN FETCH s.car c
            WHERE LOWER(i.name) IN :instructorNames
              AND LOWER(c.name) IN :carNames
              AND s.date BETWEEN :start AND :end
              AND NOT EXISTS (
                  SELECT w FROM Weekend w
                  WHERE w.instructor = s.instructor
                    AND w.day = s.date
                    AND s.timeFrom < w.timeTo
                    AND s.timeTo > w.timeFrom
              )
            """)
    List<ScheduleSlot> findAllSlots(
            @Param("instructorNames") List<String> instructorNames,
            @Param("carNames") List<String> carNames,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);


    boolean existsByDateAndTimeFromAndInstructorAndCar(
            LocalDate date,
            LocalTime timeFrom,
            Instructor instructor,
            Car car
    );

    @Query("""
    SELECT s FROM ScheduleSlot s
    JOIN FETCH s.instructor i
    JOIN FETCH s.car c
    WHERE i.id = :instructorId
       AND c.id = :carId
       AND s.date = :date
       AND s.timeFrom = :timeFrom
    """)
    Optional<ScheduleSlot> findByInstructorCarDateTime(
            @Param("instructorId") Long instructorId,
            @Param("carId") Long carId,
            @Param("date") LocalDate date,
            @Param("timeFrom") LocalTime timeFrom
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
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.instructor i
       JOIN FETCH s.student st
       WHERE LOWER(i.name) = LOWER(:instructorName)
         AND LOWER(TRIM(st.name)) = LOWER(TRIM(:studentName))
       """)
    List<ScheduleSlot> findByInstructorNameIgnoreCaseAndStudentNameIgnoreCase(
            @Param("instructorName") String instructorName,
            @Param("studentName") String studentName
    );

    @Query("""
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.instructor i
       WHERE LOWER(i.name) = LOWER(:instructorName)
       """)
    List<ScheduleSlot> findByInstructorNameIgnoreCase(
            @Param("instructorName") String instructorName
    );

    @Query("""
       SELECT s FROM ScheduleSlot s
       JOIN FETCH s.student st
       WHERE LOWER(TRIM(st.name)) = LOWER(TRIM(:studentName))
       """)
    List<ScheduleSlot> findByStudentNameIgnoreCase(
            @Param("studentName") String studentName
    );

    @Query("""
       SELECT MAX(s.date)
       FROM ScheduleSlot s
       WHERE s.instructor = :instructor
       """)
    LocalDate findMaxDateByInstructor(@Param("instructor") Instructor instructor);

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
       WHERE s.booked = true
         AND LOWER(i.name) = LOWER(:instructorName)
       """)
    List<ScheduleSlot> findAllBookedSlotsByInstructorName(
            @Param("instructorName") String instructorName
    );

    @Query("SELECT DISTINCT st.name FROM ScheduleSlot s JOIN s.student st WHERE st.name IS NOT NULL")
    List<String> findDistinctStudentNames();

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

    @Query("""
    SELECT s FROM ScheduleSlot s
    WHERE (:instructorIds IS NULL OR s.instructor.id IN :instructorIds)
      AND (:carIds IS NULL OR s.car.id IN :carIds)
      AND (:studentId IS NULL OR s.student.id = :studentId)
      AND (:from IS NULL OR s.date >= :from)
      AND (:to IS NULL OR s.date <= :to)
      AND (:booked IS NULL OR s.booked = :booked)
    ORDER BY s.date ASC, s.timeFrom ASC
    """)
    List<ScheduleSlot> findWithFilter(
            @Param("instructorIds") List<Long> instructorIds,
            @Param("carIds") List<Long> carIds,
            @Param("studentId") Long studentId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("booked") Boolean booked
    );
}
