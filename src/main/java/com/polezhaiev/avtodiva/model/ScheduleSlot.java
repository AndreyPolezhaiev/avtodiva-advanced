package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_slots", indexes = {
        @Index(name = "idx_instructor_date", columnList = "instructor_id, date"),
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_car_date", columnList = "car_id, date"),
        @Index(name = "idx_date_time_from_to", columnList = "date, timeFrom, timeTo"),
        @Index(name = "idx_booked_instructor", columnList = "booked, instructor_id")
})
@Data
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    @ToString.Exclude
    @ManyToOne
    private Instructor instructor;
    @ToString.Exclude
    @ManyToOne
    private Car car;
    @ToString.Exclude
    @ManyToOne
    private Student student;
    private String description;
    private String link;
    private boolean booked;
}
