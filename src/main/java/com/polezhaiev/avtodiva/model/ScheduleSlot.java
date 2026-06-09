package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_slots", indexes = {
        @Index(name = "idx_instructor_date", columnList = "instructor_id, date"),
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_car_date", columnList = "car_id, date"),
        @Index(name = "idx_date_time_from_to", columnList = "date, start_time, end_time"),
        @Index(name = "idx_booked_instructor", columnList = "booked, instructor_id")
})
@Data
@SQLDelete(sql = "UPDATE schedule_slots SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_slot_seq_gen")
    @SequenceGenerator(
            name = "schedule_slot_seq_gen",
            sequenceName = "schedule_slot_id_sequence",
            allocationSize = 50
    )
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ToString.Exclude
    @ManyToOne
    private Instructor instructor;

    @ToString.Exclude
    @ManyToOne
    private Car car;

    @ToString.Exclude
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Student student;
    private String description;
    private String link;
    private Boolean booked;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
