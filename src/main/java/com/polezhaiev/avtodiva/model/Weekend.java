package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "weekends")
@Getter @Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Weekend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weekend_seq_gen")
    @SequenceGenerator(
            name = "weekend_seq_gen",
            sequenceName = "weekend_id_seq",
            allocationSize = 20
    )
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    @ToString.Include
    private LocalDate date;
    @ToString.Include
    @Column(name = "start_time")
    private LocalTime startTime;
    @ToString.Include
    @Column(name = "end_time")
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    @ToString.Exclude
    private Instructor instructor;
}
