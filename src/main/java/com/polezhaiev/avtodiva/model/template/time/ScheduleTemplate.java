package com.polezhaiev.avtodiva.model.template.time;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "schedule_template")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ScheduleTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_template_seq_gen")
    @SequenceGenerator(
            name = "schedule_template_seq_gen",
            sequenceName = "schedule_template_id_seq",
            allocationSize = 20
    )
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "template_time_slots",
            joinColumns = @JoinColumn(name = "template_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TimeSlot> intervals;
}
