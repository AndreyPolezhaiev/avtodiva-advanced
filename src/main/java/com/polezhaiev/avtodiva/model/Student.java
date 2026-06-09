package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@SQLDelete(sql = "UPDATE students SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Size(min = 10)
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @ToString.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<ScheduleSlot> scheduleSlots;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
