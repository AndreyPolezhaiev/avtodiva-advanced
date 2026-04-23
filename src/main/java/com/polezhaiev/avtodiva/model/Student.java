package com.polezhaiev.avtodiva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Size(min = 10)
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @ToString.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleSlot> scheduleSlots;

    @PrePersist
    @PreUpdate
    public void normalizePhoneNumber() {
        if (this.phoneNumber != null) {
            this.phoneNumber = this.phoneNumber.replaceAll("[^0-9]", "");
        }
        if (this.name != null) {
            this.name = this.name.trim();
        }
    }
}
