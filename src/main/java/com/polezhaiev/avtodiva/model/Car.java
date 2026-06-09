package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "cars")
@Data
@SQLDelete(sql = "UPDATE cars SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "car",
            fetch = FetchType.LAZY
    )
    private List<ScheduleSlot> slots;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
