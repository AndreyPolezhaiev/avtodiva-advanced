package com.polezhaiev.avtodiva.model.template.time;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class TimeSlot {
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
}
