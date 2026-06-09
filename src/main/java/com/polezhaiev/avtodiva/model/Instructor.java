package com.polezhaiev.avtodiva.model;

import com.polezhaiev.avtodiva.model.template.time.ScheduleTemplate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "instructors")
@Getter @Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "UPDATE instructors SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "instructor",
            cascade = {CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Weekend> weekends;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "instructor",
            cascade = {CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ScheduleSlot> slots;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "schedule_template_id")
    private ScheduleTemplate scheduleTemplate;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
